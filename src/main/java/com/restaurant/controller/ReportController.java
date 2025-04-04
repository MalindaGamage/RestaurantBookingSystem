package com.restaurant.controller;

import com.restaurant.domain.Report;

public class ReportController {
    private ReportManager reportManager;

    public ReportController() {
        this.reportManager = new ReportManager();
    }

    public String[] getAvailableReportTypes() {
        return new String[]{"Booking", "Occupancy", "Revenue"};
    }

    public Report generateReport(String type, String parameters) {
        return reportManager.createReport(type, parameters); // UC6
    }

    public String requestExport(Report report, String format) {
        return reportManager.exportReport(report, format);
    }
}