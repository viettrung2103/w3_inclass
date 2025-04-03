package org.example;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.sql.*;

public class HelloController {

    @FXML
    private Label welcomeText;

    @FXML
    private ComboBox<String> languageSelector;

    @FXML
    private ListView<String> employeeList;

    private static final String DB_URL = "jdbc:mysql://localhost:3306/w3inclass";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "password123";

    private static String[] languages = {"English", "Français", "Español", "日本語"};
    public void initialize() {
        languageSelector.getItems().addAll(languages);
        languageSelector.setValue("English");
        languageSelector.setOnAction(event -> {
            changeLanguage();
        });
    }

    public void changeLanguage() {
        String selectedLanguage = languageSelector.getValue();
        // fetch data from database
        String selectedLanguageCode = mapLanguageStringToLanguageCode(selectedLanguage);
        fetchLocalizedData(selectedLanguageCode);

    }

    public void fetchLocalizedData(String selectedLanguageCode) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            // Your code here
            String query = "Select Key_name, translation_text from translations where language = '" + selectedLanguageCode + "'";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, selectedLanguageCode);
            ResultSet resultSet = preparedStatement.executeQuery();
            employeeList.getItems().clear();
            while(resultSet.next()) {
                String keyName = resultSet.getString("Key_name");
                String translationText = resultSet.getString("translation_text");
                employeeList.getItems().add(keyName + ": "+translationText);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public String mapLanguageStringToLanguageCode(String lanugageString) {
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
}
