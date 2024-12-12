Machine Tracking Application
Overview
The Machine Tracking Application is a JavaFX-based system designed to manage machine records efficiently. It provides a user-friendly interface to add, update, delete, and view machine information. This application is ideal for small businesses, workshops, or any environment requiring detailed equipment tracking.

Features
Machine Management:

Add new machines with their details.
Update and delete machine records.
View all machines in a clean, organized UI.
Data Export:

Export machine data as a CSV file for external use or backup.
Database Integration:

Utilizes SQLite for persistent storage.
Includes a pre-configured schema.sql file for easy database setup.
User Interface:

Built with JavaFX and modular FXML files for clean UI management.
Styled with a custom CSS for a modern look.
Resources:

Includes sample images and styles for demonstration.
Getting Started
Prerequisites
Java 11 or higher.
JavaFX SDK (23.0.1 or compatible).
SQLite for database setup (optional).
Setup Steps
Clone the Repository:

bash
Copy code
git clone https://github.com/<your-username>/MachineTrackingApp.git
Install JavaFX:

Download JavaFX SDK from GluonHQ.
Set up the JavaFX SDK in your IDE (e.g., IntelliJ IDEA or Eclipse).
Set Up the Database:

Use the schema.sql file located in /db to initialize the database.
Run the Application:

Open the project in your IDE.
Set up the JavaFX runtime arguments:
css
Copy code
--module-path <path-to-javafx-sdk> --add-modules javafx.controls,javafx.fxml
Build and run the project.
Project Structure
src/main/java/com/machinetracker: Contains all Java source files organized into packages:

interfaces: Interfaces for the application logic.
models: Machine and user-related data models.
persistence: Database management.
ui: Controllers for the user interface.
src/main/resources/ui: FXML files for the user interface.

src/main/resources/css: Stylesheets for application UI.

src/main/resources/machine_images: Sample images used in the application.

src/main/resources/db:

schema.sql: Database schema for SQLite.
Usage
Manage your machines:

Add, update, or delete machine records through the GUI.
View machine data in a clean, structured format.
Export machine data:

Use the export feature to save machine records as a CSV file for further analysis or backup.
Customize the database:

Modify the schema.sql file or integrate the application with your own database setup.
Contributing
Contributions are welcome! Please follow these steps:

Fork the repository.
Create a feature branch:
bash
Copy code
git checkout -b feature/your-feature
Commit your changes:
sql
Copy code
git commit -m "Add your feature"
Push to the branch:
bash
Copy code
git push origin feature/your-feature
Open a pull request.
License
This project is licensed under the MIT License. See the LICENSE file for details.
