package com.machinetracker.models;

import com.machinetracker.interfaces.IMachine;

public class Machine implements IMachine {
    private int machineID;
    private String name;
    private String type;
    private String location;
    private int year;

    public Machine(int machineID, String name, String type, String location, int year) {
        this.machineID = machineID;
        this.name = name;
        this.type = type;
        this.location = location;
        this.year = year;
    }

    @Override
    public void updateStatus(String newStatus) {
        System.out.println(name + " status updated to: " + newStatus);
    }

    @Override
    public void editDetails(String name, String location, int year) {
        this.name = name;
        this.location = location;
        this.year = year;
        System.out.println("Machine details updated.");
    }

    // Getters and Setters
    public int getMachineID() {
        return machineID;
    }

    public void setMachineID(int machineID) {
        this.machineID = machineID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }


}

