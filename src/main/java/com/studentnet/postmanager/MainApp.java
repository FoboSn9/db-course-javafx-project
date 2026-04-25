package com.studentnet.postmanager;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * Головний клас застосунку, що відповідає за ініціалізацію та відображення вікна JavaFX.
 */
public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("/com/studentnet/postmanager/view/main_view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 850, 600);

        primaryStage.setTitle("Student Social Network - Post Manager");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}