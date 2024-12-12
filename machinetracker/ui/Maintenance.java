package com.machinetracker.ui;

public class Maintenance {

    private int maintenanceID;
    private int machineID;
    private String description;
    private String date;
    private String status;

    public Maintenance(int maintenanceID, int machineID, String description, String date, String status) {
        this.maintenanceID = maintenanceID;
        this.machineID = machineID;
        this.description = description;
        this.date = date;
        this.status = status;
    }

    public int getMaintenanceID() {
        return maintenanceID;
    }

    public int getMachineID() {
        return machineID;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }

    public String getStatus() {
        return status;
    }
}
