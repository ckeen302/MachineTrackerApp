package com.machinetracker.models;

import com.machinetracker.interfaces.IUser;

public abstract class User implements IUser {
    private String userID;
    private String username;
    private String passwordHash;
    private String role;

    public User(String userID, String username, String passwordHash, String role) {
        this.userID = userID;
        this.username = username;
        this.passwordHash = passwordHash;
        this.role = role;
    }

    @Override
    public void login(String username, String password) {
        System.out.println(role + " logged in.");
    }

    @Override
    public void logout() {
        System.out.println(role + " logged out.");
    }

    // Getters and Setters
    public String getUserID() { return userID; }
    public String getUsername() { return username; }
    public String getRole() { return role; }
}
