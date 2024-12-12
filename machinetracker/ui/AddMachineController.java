package com.machinetracker.ui;

import com.machinetracker.persistence.SQLiteManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.File;

public class AddMachineController {

    @FXML
    private TextField nameField;

    @FXML
    private TextField typeField;

    @FXML
    private TextField locationField;

    @FXML
    private TextField yearField;

    @FXML
    private Button uploadImageButton;

    @FXML
    private ImageView imagePreview;

    private String image_path;

    @FXML
    public void initialize() {
        // Add a listener to the image property of imagePreview
        imagePreview.imageProperty().addListener((observable, oldImage, newImage) -> {
            if (newImage != null) {
                // Adjust the window size to accommodate the image preview
                Stage stage = (Stage) imagePreview.getScene().getWindow();
                stage.sizeToScene();  // Automatically adjusts the window size to fit the new content
            }
        });
    }

    @FXML
    private void handleUploadImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        File selectedFile = fileChooser.showOpenDialog(nameField.getScene().getWindow());
        if (selectedFile != null) {
            image_path = selectedFile.toURI().toString();
            Image image = new Image(image_path);
            imagePreview.setImage(image);
        }
    }

    @FXML
    private void handleSaveMachine() {
        String name = nameField.getText();
        String type = typeField.getText();
        String location = locationField.getText();
        int year;

        try {
            year = Integer.parseInt(yearField.getText());
        } catch (NumberFormatException e) {
            showAlert("Year must be a valid number.");
            return;
        }

        if (name.isEmpty() || type.isEmpty() || location.isEmpty()) {
            showAlert("Please fill in all fields.");
            return;
        }

        SQLiteManager.insertMachine(name, type, location, year, "Default Rules", image_path);

        Stage stage = (Stage) nameField.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void handleCancel() {
        Stage stage = (Stage) nameField.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
