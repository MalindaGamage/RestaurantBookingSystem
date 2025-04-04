package com.restaurant.domain;

import com.restaurant.controller.BookingController;
import com.restaurant.controller.ReportController;
import com.restaurant.controller.SystemSettingsManager;

public class Manager extends Staff {
    private int managerId;

    public Manager(int userId, String username, String password, String role, int staffId, String name, String contactInfo, int managerId,
                   BookingController bookingController, ReportController reportController) {
        super(userId, username, password, role, staffId, name, contactInfo, bookingController, reportController);
        this.managerId = managerId;
    }

    public Report generateReports(String type, String parameters) {
        return getReportController().generateReport(type, parameters); // UC6
    }

    public void manageSystemSettings() {
        new SystemSettingsManager().manageSystemSettings(); // UC11
    }

    public Staff createStaffAccount(String staffInfo) {
        // Simulate staff account creation
        return new Staff(2, "staff2", "password", "Staff", 2, "Mahela Jayawardhane", "mahela@example.com",
                getBookingController(), getReportController());
    }

    public String viewSystemStatistics() {
        // Simulate statistics generation
        return "System Statistics: 50 tables, 45 available.";
    }

    // Helper methods to access controllers (since they're private in Staff)
    private BookingController getBookingController() {
        try {
            java.lang.reflect.Field field = Staff.class.getDeclaredField("bookingController");
            field.setAccessible(true);
            return (BookingController) field.get(this);
        } catch (Exception e) {
            throw new RuntimeException("Failed to access BookingController", e);
        }
    }

    private ReportController getReportController() {
        try {
            java.lang.reflect.Field field = Staff.class.getDeclaredField("reportController");
            field.setAccessible(true);
            return (ReportController) field.get(this);
        } catch (Exception e) {
            throw new RuntimeException("Failed to access ReportController", e);
        }
    }
}