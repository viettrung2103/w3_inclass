package org.example;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class HelloController {

    @FXML
    private Label welcomeText;

    @FXML
    private ComboBox<String> languageSelector;

    @FXML
    private ListView<String> employeeList;

    @FXML
    private TextField keyNameField;

    @FXML
    private TextField newTranslationField;

    @FXML
    private Button addTranslationButton;

    @FXML
    private Label errorLabel;

    Locale locale;
    ResourceBundle rb;

    private static final String DB_URL = "jdbc:mysql://localhost:3306/w3inclass";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "password123";

    private static String[] languages = {"English", "Français", "Español", "日本語"};
    public void initialize() {
        String defaultLanguage = "English";
        String defaultLanguageCode = mapLanguageStringToLanguageCode(defaultLanguage);
        displayLocalizedUI(defaultLanguageCode);
        languageSelector.getItems().addAll(languages);
        languageSelector.setValue(defaultLanguage);
        changeLanguage();
        languageSelector.setOnAction(event -> {
            changeLanguage();
        });
    }

    public void changeLanguage() {
        String selectedLanguage = languageSelector.getValue();
        // fetch data from database
        String selectedLanguageCode = mapLanguageStringToLanguageCode(selectedLanguage);
        fetchLocalizedData(selectedLanguageCode);
        displayLocalizedUI(selectedLanguageCode);

    }

    public void fetchLocalizedData(String selectedLanguageCode) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            // Your code here
            System.out.println("fetch localized data");
            String query = "SELECT Key_name, translation_text FROM translations WHERE Language_code = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, selectedLanguageCode);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<String> employees = new ArrayList<>();
            while(resultSet.next()) {
                String keyName = resultSet.getString("Key_name");
                String translationText = resultSet.getString("translation_text");
                String languageText = keyName +" : "+translationText;
//                employeeList.getItems().add(keyName + ": "+translationText);
                employees.add(languageText);
            }
            Platform.runLater(() -> {
                employeeList.getItems().clear();
                employeeList.getItems().addAll(employees);
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public String mapLanguageStringToLanguageCode(String lanugageString) {
        System.out.println(lanugageString);
        System.out.println("true: "+lanugageString.equals("日本語"));
        switch (lanugageString){
            case "English":
                return "en";
            case "Français":
                return "fr";
            case "Español":
                return "es";
            case "日本語":
                return "zh";
            default:
                return "en";
        }
    }

    public Locale getLocale(String languageCode) {
//        String languageCode = mapLanguageStringToLanguageCode(LanguageString);
        switch (languageCode){
            case "en":
                return new Locale("en","US");
            case "fr":
                return new Locale("fr","FR");
            case "es":
                return new Locale("es","ES");
            case "zh":
                return new Locale("zh","CN");
            default:
                return new Locale("en","US");
        }
    }

    @FXML
    public void addButtonClick() {

        String selectedLanguage = languageSelector.getValue();
        String selectedLanguageCode = mapLanguageStringToLanguageCode(selectedLanguage);
        String inputKeyName = keyNameField.getText();
        String inputTranslationText = newTranslationField.getText();
        if (keyNameField.equals("")&& newTranslationField.equals("")) {
            displayLocalizedError();
        }else {
        upsertKeyAndTranslation(selectedLanguageCode, inputKeyName, inputTranslationText);
        fetchLocalizedData(selectedLanguageCode);
        keyNameField.clear();
        newTranslationField.clear();
        }
    }

    public void upsertKeyAndTranslation(
            String selectedLanguageCode,
            String inputKeyName,
            String inputTranslationText) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);) {
        String query = "INSERT INTO translations (Key_name,Language_code, translation_text) VALUES (?,?,?)";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1,inputKeyName);
            preparedStatement.setString(2,selectedLanguageCode);
            preparedStatement.setString(3,inputTranslationText);
            preparedStatement.execute();
            System.out.println("Translation added");
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void displayLocalizedUI(String languageCode) {
//        String languageCode = mapLanguageStringToLanguageCode(languageString);

        System.out.println("selectedLanguageCode: "+languageCode);
        Locale selectedLocale = getLocale(languageCode);
        System.out.println("selectedLocale: "+selectedLocale);
        rb = ResourceBundle.getBundle("messages",selectedLocale);
        welcomeText.setText(rb.getString("welcomeText"));
        addTranslationButton.setText(rb.getString("addTranslationButton"));
        keyNameField.setPromptText(rb.getString("keyNameField"));
        newTranslationField.setPromptText(rb.getString("newTranslationField"));
        refreshLocalizedError();
    }

    public void displayLocalizedError(){
        String errorText = rb.getString("errorText");
        errorLabel.setText(errorText);
    }

    public void refreshLocalizedError(){
        if (!errorLabel.getText().equals("")) {
            displayLocalizedError();
        }
    }
}
