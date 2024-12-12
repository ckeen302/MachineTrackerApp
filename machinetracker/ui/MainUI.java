package com.machinetracker.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainUI extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/ManageMachines.fxml"));
        Parent root = loader.load();

        // Set up the scene
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);

        // Set initial window size
        primaryStage.setWidth(1000);
        primaryStage.setHeight(800);

        // Set minimum window size
        primaryStage.setMinWidth(800);
        primaryStage.setMinHeight(600);

        // Set the title of the window
        primaryStage.setTitle("Machine Tracker");

        // Apply CSS to the scene
        scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());

        // Show the window
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}