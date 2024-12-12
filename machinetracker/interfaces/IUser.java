package com.machinetracker.interfaces;

public interface IUser {
    void login(String username, String password);
    void logout();
}