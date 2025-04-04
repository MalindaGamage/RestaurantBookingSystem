package com.restaurant.controller;

import com.restaurant.domain.Booking;
import com.restaurant.domain.Report;

public class ReportManager {
    public String[] retrieveReportTypes() {
        return new String[]{"Booking", "Occupancy", "Revenue"};
    }

    public Report createReport(String type, String parameters) {
        Report report = new Report("R" + System.currentTimeMillis(), type, parameters);
        StringBuilder data = new StringBuilder();
        for (Booking booking : BookingSystem.getInstance().getBookings()) {
            data.append(booking.getDetails()).append("\n");
        }
        report.update(new Booking("Temp", null, null, null, 0, "")); // Trigger update
        return report;
    }

    public String exportReport(Report report, String format) {
        return report.export(format);
    }
}