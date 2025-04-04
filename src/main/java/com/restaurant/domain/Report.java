package com.restaurant.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Report implements BookingObserver {
    private String reportId;
    private String type;
    private String parameters;
    private LocalDateTime generationDate;
    private List<String> data;

    public Report(String reportId, String type, String parameters) {
        this.reportId = reportId;
        this.type = type;
        this.parameters = parameters;
        this.generationDate = LocalDateTime.now();
        this.data = new ArrayList<>();
    }

    @Override
    public void update(Booking booking) {
        this.data.add(booking.getDetails() + "\n");
        System.out.println("Report updated: " + booking.getDetails());
    }

    public void display() {
        System.out.println("Report ID: " + reportId + ", Type: " + type + ", Data:\n" + String.join("", data));
    }

    public String export(String format) {
        return "Exported report in " + format + ": " + String.join("", data);
    }

    public boolean print() {
        System.out.println("Printing report: " + String.join("", data));
        return true;
    }

    // Updated getters
    public String getReportId() {
        return reportId;
    }

    public String getReportType() {
        return type;
    }

    public String getParameters() {
        return parameters;
    }

    public List<String> getData() {
        return Collections.unmodifiableList(data);
    }

    public void setData(List<String> data) {
        this.data = data;
    }
}