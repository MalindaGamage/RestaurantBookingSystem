package com.restaurant.domain;

import java.time.LocalDateTime;

public class WalkinRecord {
    private String recordId;
    private Table table;
    private String customerName;
    private int guests;
    private LocalDateTime arrivalTime;

    public WalkinRecord(String recordId, Table table, String customerName, int guests, LocalDateTime arrivalTime) {
        this.recordId = recordId;
        this.table = table;
        this.customerName = customerName;
        this.guests = guests;
        this.arrivalTime = arrivalTime;
        table.updateStatus("Occupied");
    }

    public boolean updateStatus(String status) {
        return table.updateStatus(status);
    }

    public Booking convertToBooking() {
        // Simulate conversion to a booking
        return new Booking(recordId, table, new Customer(0, customerName, "", ""), arrivalTime, guests, "");
    }

    // Added getter for table
    public Table getTable() {
        return table;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getRecordId() {
        return recordId;
    }
}