package com.machinetracker.persistence;

import com.machinetracker.ui.Machine;
import com.machinetracker.ui.Maintenance;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.ResultSet;



public class SQLiteManager {
    private static final String DB_URL = "jdbc:sqlite:src/main/resources/db/machinetracker.db";

    public static Connection connect() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(DB_URL);
            System.out.println("Connected to SQLite database.");
        }
        catch (SQLException e) {
            System.err.println("Connection failed: " + e.getMessage());
        }
        return connection;
    }

    public static void createTables() {
        String userTable = """
                CREATE TABLE IF NOT EXISTS users (
                    user_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    username TEXT NOT NULL,
                    password_hash TEXT NOT NULL,
                    role TEXT NOT NULL
                );
                """;

        String machineTable = """
                CREATE TABLE IF NOT EXISTS machines (
                    machine_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT NOT NULL,
                    type TEXT,
                    location TEXT,
                    status TEXT,
                    year INTEGER,
                    tournament_eligible BOOLEAN,
                    rules TEXT
                );
                """;
        String maintenanceTable = """
            CREATE TABLE IF NOT EXISTS maintenance (
                maintenance_id INTEGER PRIMARY KEY AUTOINCREMENT,
                machine_id INTEGER,
                description TEXT NOT NULL,
                date TEXT NOT NULL,
                status TEXT NOT NULL,
                FOREIGN KEY (machine_id) REFERENCES machines (machine_id)
            );
            """;

        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(userTable);
            stmt.execute(machineTable);
            System.out.println("Tables created successfully.");
        }
        catch (Exception e) {
            System.err.println("Table creation failed: " + e.getMessage());
        }
    }

    public static int getLowestUnusedMachineID() {
        String sql = "SELECT machine_id FROM machines ORDER BY machine_id ASC";

        try (Connection conn = connect(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            int expectedID = 1; // Start looking for the lowest ID starting from 1

            while (rs.next()) {
                int currentID = rs.getInt("machine_id");
                if (currentID != expectedID) {
                    // If the current ID is not the same as expected, we found a gap
                    return expectedID;
                }
                expectedID++;
            }

            // If we reach here, it means all IDs are sequential up to the highest value, so the lowest unused is the next one
            return expectedID;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return 1; // Default to 1 if there's an error
        }
    }


    public static void insertMachine(String name, String type, String location, int year, String rules, String image_path) {
        int newID = getLowestUnusedMachineID(); // Get the lowest unused ID
        String sql = "INSERT INTO machines(machine_id, name, type, location, status, year, tournament_eligible, rules, image_path) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = connect(); java.sql.PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, newID);
            pstmt.setString(2, name);
            pstmt.setString(3, type);
            pstmt.setString(4, location);
            pstmt.setString(5, "active");
            pstmt.setInt(6, year);
            pstmt.setBoolean(7, true);
            pstmt.setString(8, rules);
            pstmt.setString(9, image_path);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    public static ObservableList<Machine> getAllMachines() {
        ObservableList<Machine> machineList = FXCollections.observableArrayList();
        String query = "SELECT machine_id, name, type, location, year, image_path FROM machines";

        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int id = rs.getInt("machine_id");
                String name = rs.getString("name");
                String type = rs.getString("type");
                String location = rs.getString("location");
                int year = rs.getInt("year");
                String image_path = rs.getString("image_path");  // Get the image path from the database

                // Create a Machine object and add it to the list
                Machine machine = new Machine(id, name, type, location, year, image_path);
                machineList.add(machine);
            }
        } catch (SQLException e) {
            System.err.println("Retrieve machines failed: " + e.getMessage());
        }

        return machineList;
    }

    public static void updateMachineStatus(int machine_id, String newStatus) {
        String sql = "UPDATE machines SET status = ? WHERE machine_id = ?";

        try (Connection conn = connect();
             java.sql.PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newStatus);
            pstmt.setInt(2, machine_id);
            pstmt.executeUpdate();
            System.out.println("Machine status updated successfully.");
        }
        catch (Exception e) {
            System.err.println("Update machine status failed: " + e.getMessage());
        }
    }

    public static void deleteMachine(int machine_id) {
        String sql = "DELETE FROM machines WHERE machine_id = ?";

        try (Connection conn = connect();
             java.sql.PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, machine_id);
            pstmt.executeUpdate();
            System.out.println("Machine deleted successfully.");
        } catch (Exception e) {
            System.err.println("Delete machine failed: " + e.getMessage());
        }
    }

    public static void updateMachineDetails(int machine_id, String name, String location, int year, String image_path) {
        String sql = "UPDATE machines SET name = ?, location = ?, year = ?, image_path = ? WHERE machine_id = ?";

        try (Connection conn = connect(); java.sql.PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, location);
            pstmt.setInt(3, year);
            pstmt.setString(4, image_path);
            pstmt.setInt(5, machine_id);
            pstmt.executeUpdate();
            System.out.println("Machine updated successfully.");
        } catch (Exception e) {
            System.err.println("Update machine failed: " + e.getMessage());
        }
    }
}