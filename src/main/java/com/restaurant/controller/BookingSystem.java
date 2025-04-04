package com.restaurant.controller;

import com.restaurant.domain.*;
import java.util.ArrayList;
import java.util.List;

public class BookingSystem {
    private static BookingSystem instance;
    private String systemName;
    private String version;
    private SystemSettings settings;
    private List<Table> tables;
    private List<Booking> bookings;
    private BookingController bookingController;
    private ReportController reportController;
    private SystemSettingsManager settingsManager;
    private static final int TOTAL_TABLES = 50;

    private BookingSystem() {
        this.systemName = "Restaurant Table Booking System";
        this.version = "1.0";
        this.settings = new SystemSettings();
        this.tables = new ArrayList<>();
        this.bookings = new ArrayList<>();
        initializeTables();
        this.bookingController = new BookingController();
        this.reportController = new ReportController();
        this.settingsManager = new SystemSettingsManager();
    }

    public static BookingSystem getInstance() {
        if (instance == null) {
            instance = new BookingSystem();
        }
        return instance;
    }

    private void initializeTables() {
        // Initialize 50 tables with varying capacities (2, 4, 6, 8)
        for (int i = 1; i <= TOTAL_TABLES; i++) {
            int capacity = (i % 4 == 0) ? 8 : (i % 3 == 0) ? 6 : (i % 2 == 0) ? 4 : 2;
            tables.add(new Table(i, capacity, "Section " + (i % 5)));
        }
    }

    public void initialize() {
        System.out.println(systemName + " v" + version + " initialized with " + TOTAL_TABLES + " tables.");
    }

    public boolean backup() {
        System.out.println("Backing up system data...");
        return true;
    }

    public boolean restore() {
        System.out.println("Restoring system data...");
        return true;
    }

    public boolean updateSettings(String settings) {
        return this.settings.update(settings);
    }

    public BookingController getBookingController() {
        return bookingController;
    }

    public ReportController getReportController() {
        return reportController;
    }

    public List<Table> getTables() {
        return tables;
    }

    public List<Booking> getBookings() {
        return bookings;
    }
}