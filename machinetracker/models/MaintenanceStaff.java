package com.machinetracker.models;

public class MaintenanceStaff extends User {
    public MaintenanceStaff(String userID, String username, String passwordHash) {
        super(userID, username, passwordHash, "MaintenanceStaff");
    }

    public void logMaintenance(Machine machine, String notes) {
        System.out.println("Maintenance logged for " + machine.getName() + ": " + notes);
    }
}
