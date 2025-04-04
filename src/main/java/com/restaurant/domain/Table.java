package com.restaurant.domain;

import java.util.List;

public class Table {
    private int tableId;
    private int capacity;
    private String location;
    private String status;
    private List<TimeSlot> availabilitySchedule;

    public Table(int tableId, int capacity, String location) {
        this.tableId = tableId;
        this.capacity = capacity;
        this.location = location;
        this.status = "Available";
    }

    public List<TimeSlot> getAvailabilitySchedule() {
        return availabilitySchedule;
    }

    public boolean isAvailable(TimeSlot timeSlot) {
        return status.equals("Available");
    }

    public boolean reserve(TimeSlot timeSlot) {
        if (isAvailable(timeSlot)) {
            status = "Reserved";
            return true;
        }
        return false;
    }

    public boolean release(TimeSlot timeSlot) {
        status = "Available";
        return true;
    }

    public boolean updateStatus(String status) {
        this.status = status;
        return true;
    }

    public String getTableId() {
        return "T" + tableId;
    }

    public int getCapacity() {
        return capacity;
    }

    public String getStatus() {
        return status;
    }
}
