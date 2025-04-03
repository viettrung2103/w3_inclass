package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class HelloView extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/hello-view.fxml"));
        Parent root = fxmlLoader.load();
        stage.setScene(new Scene(root));
        stage.setTitle("Job Titles");
        stage.setResizable(false);
        stage.show();
    }
}
