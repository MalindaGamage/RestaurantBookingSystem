package com.restaurant.domain;

import java.time.LocalDateTime;

public class Report implements BookingObserver {
    private String reportId;
    private String type;
    private String parameters;
    private LocalDateTime generationDate;
    private String data;

    public Report(String reportId, String type, String parameters) {
        this.reportId = reportId;
        this.type = type;
        this.parameters = parameters;
        this.generationDate = LocalDateTime.now();
        this.data = "";
    }

    @Override
    public void update(Booking booking) {
        this.data += booking.getDetails() + "\n";
        System.out.println("Report updated: " + booking.getDetails());
    }

    public void display() {
        System.out.println("Report ID: " + reportId + ", Type: " + type + ", Data:\n" + data);
    }

    public String export(String format) {
        return "Exported report in " + format + ": " + data;
    }

    public boolean print() {
        System.out.println("Printing report: " + data);
        return true;
    }

    public String getData() {
        return data;
    }

    public String getReportId() {
        return reportId;
    }

}