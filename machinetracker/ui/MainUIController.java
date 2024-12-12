package com.machinetracker.ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainUIController {

    @FXML
    public void navigateToManageMachines() throws IOException {
        // Load the Manage Machines screen
        Parent root = FXMLLoader.load(getClass().getResource("/ui/ManageMachines.fxml"));
        Stage stage = new Stage();
        stage.setTitle("Manage Machines");
        stage.setScene(new Scene(root));
        stage.show();
    }
}
