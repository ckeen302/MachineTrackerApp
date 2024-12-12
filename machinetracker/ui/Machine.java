package com.machinetracker.ui;

public class Machine {
    private int machine_id;
    private String name;
    private String type;
    private String location;
    private int year;
    private String imagePath;

    public Machine(int machine_id, String name, String type, String location, int year, String imagePath) {
        this.machine_id = machine_id;
        this.name = name;
        this.type = type;
        this.location = location;
        this.year = year;
        this.imagePath = imagePath;
    }

    // Getters
    public int getMachineID() {
        return machine_id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getLocation() {
        return location;
    }

    public int getYear() {
        return year;
    }

    public String getImagePath() {
        return imagePath;
    }

    // Setters

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setImagePath(String imagePath) { this.imagePath = imagePath; }
}