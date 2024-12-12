package com.machinetracker.ui;

import com.machinetracker.persistence.SQLiteManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class DeleteMachineController {

    private int machineID;

    @FXML
    private Label confirmationLabel;

    public void setMachineID(int machineID) {
        this.machineID = machineID;
    }

    @FXML
    private void handleDelete() {
        SQLiteManager.deleteMachine(machineID);

        // Close the window after deleting the machine
        Stage stage = (Stage) confirmationLabel.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void handleCancel() {
        // Close the window without deleting
        Stage stage = (Stage) confirmationLabel.getScene().getWindow();
        stage.close();
    }
}
