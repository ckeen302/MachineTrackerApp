package com.machinetracker.ui;

import com.machinetracker.persistence.SQLiteManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javafx.stage.FileChooser;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.stream.Collectors;
import java.util.Set;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.Scene;
import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Modality;
import javafx.scene.control.cell.TextFieldTableCell;

public class ManageMachinesController {

    @FXML
    private TextField searchField;

    @FXML
    private TableView<Machine> machineTable;

    @FXML
    private TableColumn<Machine, Integer> idColumn;

    @FXML
    private TableColumn<Machine, String> nameColumn;

    @FXML
    private TableColumn<Machine, String> typeColumn;

    @FXML
    private TableColumn<Machine, String> locationColumn;

    @FXML
    private TableColumn<Machine, Integer> yearColumn;

    @FXML
    private ComboBox<String> filterTypeComboBox;

    @FXML
    private ComboBox<String> filterLocationComboBox;

    @FXML
    private Button backButton;

    private ObservableList<Machine> machineList;

    @FXML
    private Button uploadImageButton;

    @FXML
    private ImageView imagePreview;

    @FXML
    private ImageView selectedMachineImageView;  // ImageView for selected machine

    @FXML
    private TextField nameField;

    @FXML
    private TextField typeField;

    @FXML
    private TextField locationField;

    @FXML
    private TextField yearField;

    private String imagePath;

    @FXML
    private SplitPane splitPane;

    @FXML
    public void initialize() {
        
        // Set up the table columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("machineID"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("year"));

        // Set TableView to use constrained resize policy
        machineTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Bind columns to the width of the table view for dynamic resizing
        idColumn.prefWidthProperty().bind(machineTable.widthProperty().multiply(0.05));
        nameColumn.prefWidthProperty().bind(machineTable.widthProperty().multiply(0.35));
        typeColumn.prefWidthProperty().bind(machineTable.widthProperty().multiply(0.25));
        locationColumn.prefWidthProperty().bind(machineTable.widthProperty().multiply(0.25));
        yearColumn.prefWidthProperty().bind(machineTable.widthProperty().multiply(0.1));

        // Make table columns editable
        machineTable.setEditable(true);
        nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        typeColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        locationColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        yearColumn.setCellFactory(TextFieldTableCell.forTableColumn(new javafx.util.converter.IntegerStringConverter()));

        // Commit changes to the database when editing is finished
        nameColumn.setOnEditCommit(event -> {
            Machine machine = event.getRowValue();
            machine.setName(event.getNewValue());
            SQLiteManager.updateMachineDetails(machine.getMachineID(), machine.getName(), machine.getLocation(), machine.getYear(), machine.getImagePath());
        });

        typeColumn.setOnEditCommit(event -> {
            Machine machine = event.getRowValue();
            machine.setType(event.getNewValue());
            SQLiteManager.updateMachineDetails(machine.getMachineID(), machine.getName(), machine.getLocation(), machine.getYear(), machine.getImagePath());
        });

        locationColumn.setOnEditCommit(event -> {
            Machine machine = event.getRowValue();
            machine.setLocation(event.getNewValue());
            SQLiteManager.updateMachineDetails(machine.getMachineID(), machine.getName(), machine.getLocation(), machine.getYear(), machine.getImagePath());
        });

        yearColumn.setOnEditCommit(event -> {
            Machine machine = event.getRowValue();
            machine.setYear(event.getNewValue());
            SQLiteManager.updateMachineDetails(machine.getMachineID(), machine.getName(), machine.getLocation(), machine.getYear(), machine.getImagePath());
        });

        // Load machine data from the database
        loadMachineData();

        // Populate filter options dynamically
        populateFilters();

        // Handle selecting an item from the table
        machineTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showMachineDetails(newValue));

        // Set SplitPane initial divider position to achieve 2/3 - 1/3 split
        double fixedDividerPosition = 0.60;
        splitPane.setDividerPositions(fixedDividerPosition);

        // Lock the divider by adding a listener to reset its position
        splitPane.getDividers().get(0).positionProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.doubleValue() != fixedDividerPosition) {
                splitPane.setDividerPositions(fixedDividerPosition);
            }
        });

        // Configure the ImageView to maintain aspect ratio while fitting the container
        selectedMachineImageView.setPreserveRatio(true);

        // Bind fitWidth and fitHeight properties to make the ImageView scale within the container
        selectedMachineImageView.fitWidthProperty().bind(
                splitPane.widthProperty().multiply(0.45).subtract(5) // Set 30% of the SplitPane width, with padding
        );

        selectedMachineImageView.fitHeightProperty().bind(
                splitPane.heightProperty().subtract(5) // Full height of the SplitPane, with some padding
        );
    }

    private void loadMachineData() {
        machineList = SQLiteManager.getAllMachines();
        if (machineList != null) {
            machineTable.setItems(machineList);
        } else {
            machineList = FXCollections.observableArrayList(); // Ensure machineList is initialized even if no data is available
            machineTable.setItems(machineList);
        }
    }

    private void populateFilters() {
        if (machineList == null || machineList.isEmpty()) {
            return;
        }

        // Get distinct types from machineList
        Set<String> types = machineList.stream()
                .map(Machine::getType)
                .filter(type -> type != null && !type.isEmpty())
                .collect(Collectors.toSet());
        filterTypeComboBox.setItems(FXCollections.observableArrayList(types));

        // Get distinct locations from machineList
        Set<String> locations = machineList.stream()
                .map(Machine::getLocation)
                .filter(location -> location != null && !location.isEmpty())
                .collect(Collectors.toSet());
        filterLocationComboBox.setItems(FXCollections.observableArrayList(locations));
    }

    private void showMachineDetails(Machine machine) {
        if (machine != null) {
            // Update the image view with the machine's image
            String imagePath = machine.getImagePath();
            if (imagePath != null && !imagePath.isEmpty()) {
                try {
                    Image image = new Image(imagePath);
                    selectedMachineImageView.setImage(image);
                } catch (IllegalArgumentException e) {
                    // Handle the case where the image cannot be loaded (e.g., file not found)
                    selectedMachineImageView.setImage(null);
                }
            } else {
                selectedMachineImageView.setImage(null);
            }
        } else {
            // Clear the image if no machine is selected
            selectedMachineImageView.setImage(null);
        }
    }

    @FXML
    private void handleApplyFilter() {
        String selectedType = filterTypeComboBox.getValue();
        String selectedLocation = filterLocationComboBox.getValue();

        ObservableList<Machine> filteredList = FXCollections.observableArrayList(machineList);

        if (selectedType != null && !selectedType.isEmpty()) {
            filteredList = filteredList.filtered(machine -> machine.getType().equalsIgnoreCase(selectedType));
        }

        if (selectedLocation != null && !selectedLocation.isEmpty()) {
            filteredList = filteredList.filtered(machine -> machine.getLocation().equalsIgnoreCase(selectedLocation));
        }

        machineTable.setItems(filteredList);
    }

    @FXML
    private void handleClearFilter() {
        filterTypeComboBox.setValue(null);
        filterLocationComboBox.setValue(null);
        machineTable.setItems(machineList);
    }

    @FXML
    private void handleViewDetails() {
        Machine selectedMachine = machineTable.getSelectionModel().getSelectedItem();

        if (selectedMachine == null) {
            showAlert("Please select a machine to view details.");
            return;
        }

        // Create a new stage for the detailed view
        Stage detailStage = new Stage();
        detailStage.setTitle("Machine Details");

        // Create a grid to display the details
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 20, 20, 20));

        // Column constraints to align labels and values
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(30);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(70);
        grid.getColumnConstraints().addAll(col1, col2);

        // Add machine details to the grid
        grid.add(new Label("Machine ID:"), 0, 0);
        grid.add(new Label(String.valueOf(selectedMachine.getMachineID())), 1, 0);

        grid.add(new Label("Name:"), 0, 1);
        grid.add(new Label(selectedMachine.getName()), 1, 1);

        grid.add(new Label("Type:"), 0, 2);
        grid.add(new Label(selectedMachine.getType()), 1, 2);

        grid.add(new Label("Location:"), 0, 3);
        grid.add(new Label(selectedMachine.getLocation()), 1, 3);

        grid.add(new Label("Year:"), 0, 4);
        grid.add(new Label(String.valueOf(selectedMachine.getYear())), 1, 4);

        // Add the image (if available)
        String imagePath = selectedMachine.getImagePath();
        if (imagePath != null && !imagePath.isEmpty()) {
            try {
                Image image = new Image(imagePath);
                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(200);    // Set the preferred width for the image
                imageView.setPreserveRatio(true);  // Keep the aspect ratio of the image

                grid.add(new Label("Image:"), 0, 5);
                grid.add(imageView, 1, 5);
            } catch (IllegalArgumentException e) {
                // Handle the case where the image cannot be loaded (e.g., file not found)
                grid.add(new Label("Image:"), 0, 5);
                grid.add(new Label("Image not available"), 1, 5);
            }
        }

        // Set the scene and show the stage
        Scene scene = new Scene(grid);
        detailStage.setScene(scene);
        detailStage.show();
    }

    @FXML
    private void handleReset() {
        // Reset the search field and reload all machines
        searchField.clear();
        loadMachineData();
    }

    @FXML
    private void handleSearch() {
        String searchTerm = searchField.getText().toLowerCase();

        if (searchTerm.isEmpty()) {
            showAlert("Please enter a name to search.");
            return;
        }

        ObservableList<Machine> filteredList = FXCollections.observableArrayList();

        for (Machine machine : machineList) {
            if (machine.getName().toLowerCase().contains(searchTerm)) {
                filteredList.add(machine);
            }
        }

        machineTable.setItems(filteredList);
    }

    @FXML
    private void handleExportToCSV() {
        if (machineList == null || machineList.isEmpty()) {
            showAlert("No data available to export.");
            return;
        }

        // Create a new FileChooser instance
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save CSV File");

        // Set the initial directory to the user's home directory
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));

        // Set the extension filters to ensure only CSV files are shown
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));

        // Show the save dialog
        File file = fileChooser.showSaveDialog(machineTable.getScene().getWindow());

        // Check if a file was selected
        if (file != null) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                // Write headers
                writer.write("ID,Name,Type,Location,Year\n");

                // Write each machine data
                for (Machine machine : machineList) {
                    writer.write(String.format("%d,%s,%s,%s,%d\n",
                            machine.getMachineID(),
                            machine.getName(),
                            machine.getType(),
                            machine.getLocation(),
                            machine.getYear()));
                }

                // Show success message
                showAlert("Data exported successfully to " + file.getAbsolutePath());
            } catch (IOException e) {
                // Handle exceptions
                showAlert("Error exporting data: " + e.getMessage());
            }
        } else {
            // No file selected (user cancelled the save)
            showAlert("Export cancelled.");
        }
    }

    @FXML
    private void handleBack() {
        // Close the current stage to go back to the main dashboard
        Stage stage = (Stage) backButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void handleAddMachineWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/AddMachine.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Add Machine");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL); // Block interaction with other windows until closed
            stage.showAndWait();

            // Get the image path (set when user uploads the image)
            if (imagePath != null) {
                // Add the machine to the database with image path
                SQLiteManager.insertMachine(nameField.getText(), typeField.getText(), locationField.getText(), Integer.parseInt(yearField.getText()), "Default Rules", imagePath);
            }

            // Reload data to refresh the table after adding a machine
            loadMachineData();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDeleteMachineWindow() {
        Machine selectedMachine = machineTable.getSelectionModel().getSelectedItem();
        if (selectedMachine == null) {
            showAlert("Please select a machine to delete.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/DeleteMachineConfirmation.fxml"));
            Parent root = loader.load();

            DeleteMachineController controller = loader.getController();
            controller.setMachineID(selectedMachine.getMachineID());

            Stage stage = new Stage();
            stage.setTitle("Delete Machine");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL); // Block interaction with other windows until closed
            stage.showAndWait();

            // Reload data to refresh the table after deleting a machine
            loadMachineData();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Helper method to get the file extension.
     * @param file The file to extract the extension from.
     * @return The file extension, including the dot (e.g., ".png"), or an empty string if none found.
     */
    private String getFileExtension(File file) {
        String fileName = file.getName();
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex == -1) ? "" : fileName.substring(dotIndex);
    }





    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handleChangeImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image for Machine");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        File selectedFile = fileChooser.showOpenDialog(machineTable.getScene().getWindow());
        if (selectedFile != null) {
            String imagePath = selectedFile.toURI().toString();
            selectedMachineImageView.setImage(new Image(imagePath));
            Machine selectedMachine = machineTable.getSelectionModel().getSelectedItem();
            if (selectedMachine != null) {
                selectedMachine.setImagePath(imagePath);
                // Update the machine details in the database with the new image path
                SQLiteManager.updateMachineDetails(
                        selectedMachine.getMachineID(),
                        selectedMachine.getName(),
                        selectedMachine.getLocation(),
                        selectedMachine.getYear(),
                        imagePath
                );
            }
        }
    }

    @FXML
    private void handleUploadImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        File selectedFile = fileChooser.showOpenDialog(machineTable.getScene().getWindow());
        if (selectedFile != null) {
            try {
                // Get the selected machine to generate the new file name
                Machine selectedMachine = machineTable.getSelectionModel().getSelectedItem();
                if (selectedMachine == null) {
                    showAlert("Please select a machine to upload an image for.");
                    return;
                }

                // Path to the resources folder during development
                String resourceDirectory = "src/main/resources/machine_images";

                // Create the images directory if it doesn't exist
                File imagesDir = new File(resourceDirectory);
                if (!imagesDir.exists()) {
                    imagesDir.mkdirs();
                }

                // Get extension and create new file name
                String extension = getFileExtension(selectedFile);
                String newFileName = selectedMachine.getName().replaceAll("\\s+", "_") + "_" + selectedMachine.getMachineID() + extension;
                String destinationPath = resourceDirectory + File.separator + newFileName;
                File destinationFile = new File(destinationPath);

                // Copy file
                Files.copy(selectedFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

                // Set image path
                imagePath = destinationFile.toURI().toString();
                Image image = new Image(imagePath);
                selectedMachineImageView.setImage(image); // Show a preview of the image


                // Update the machine's image path
                selectedMachine.setImagePath(imagePath);
                SQLiteManager.updateMachineDetails(selectedMachine.getMachineID(), selectedMachine.getName(), selectedMachine.getLocation(), selectedMachine.getYear(), imagePath);

            } catch (IOException e) {
                showAlert("Error uploading image: " + e.getMessage());
            }
        }
    }
}
