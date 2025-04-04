package com.restaurant.controller;

import com.restaurant.domain.*;
import com.restaurant.strategy.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TableManager {
    private TableAssignmentStrategy strategy;

    public TableManager() {
        // Default strategy
        this.strategy = new OptimalCapacityStrategy();
    }

    public void setStrategy(TableAssignmentStrategy strategy) {
        this.strategy = strategy;
    }

    public Table[] findAvailableTables(String date, String time, int guests) {
        LocalDateTime dateTime = LocalDateTime.parse(date + "T" + time);
        List<Table> availableTables = new ArrayList<>();
        for (Table table : BookingSystem.getInstance().getTables()) {
            if (table.isAvailable(new TimeSlot(dateTime)) && table.getCapacity() >= guests) {
                availableTables.add(table);
            }
        }
        return strategy.assignTables(availableTables, guests);
    }

    public Table[] findCurrentlyAvailableTables(int guests) {
        LocalDateTime now = LocalDateTime.now();
        List<Table> availableTables = new ArrayList<>();
        for (Table table : BookingSystem.getInstance().getTables()) {
            if (table.isAvailable(new TimeSlot(now)) && table.getCapacity() >= guests) {
                availableTables.add(table);
            }
        }
        return strategy.assignTables(availableTables, guests);
    }

    public boolean reserveTable(String tableId, TimeSlot timeSlot) {
        for (Table table : BookingSystem.getInstance().getTables()) {
            if (table.getTableId().equals(tableId)) {
                return table.reserve(timeSlot);
            }
        }
        return false;
    }

    public boolean releaseTable(String tableId, TimeSlot timeSlot) {
        for (Table table : BookingSystem.getInstance().getTables()) {
            if (table.getTableId().equals(tableId)) {
                return table.release(timeSlot);
            }
        }
        return false;
    }

    public boolean updateTableStatus(String tableId, String status, LocalDateTime timeSlot) {
        for (Table table : BookingSystem.getInstance().getTables()) {
            if (table.getTableId().equals(tableId)) {
                return table.updateStatus(status);
            }
        }
        return false;
    }

    public boolean updateTableStatuses(List<String> tableIds, String status, LocalDateTime timeSlot) {
        boolean allSuccessful = true;
        for (String tableId : tableIds) {
            boolean success = false;
            for (Table table : BookingSystem.getInstance().getTables()) {
                if (table.getTableId().equals(tableId)) {
                    success = table.updateStatus(status);
                    break;
                }
            }
            if (!success) {
                allSuccessful = false; // Even one failure means the operation isn’t fully successful
            }
        }
        return allSuccessful;
    }

    public String getTableLayout() {
        return "Table layout: " + BookingSystem.getInstance().getTables().size() + " tables.";
    }
}