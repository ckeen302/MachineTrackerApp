package com.machinetracker.interfaces;

public interface IMachine {
    void updateStatus(String newStatus);
    void editDetails(String name, String location, int year);
}