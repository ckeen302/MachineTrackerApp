package com.machinetracker.models;

public class Owner extends User {
    public Owner(String userID, String username, String passwordHash) {
        super(userID, username, passwordHash, "Owner");
    }

    public void addMachine(Machine machine) {
        System.out.println("Machine " + machine.getName() + " added.");
    }
}
