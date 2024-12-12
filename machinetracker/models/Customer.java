package com.machinetracker.models;

public class Customer extends User {
    public Customer(String userID, String username, String passwordHash) {
        super(userID, username, passwordHash, "Customer");
    }

    public void searchMachine(String criteria) {
        System.out.println("Searching for machines with: " + criteria);
    }
}

