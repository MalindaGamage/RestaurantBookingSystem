package com.restaurant.domain;

import java.time.LocalDateTime;

public class WaitingListEntry {
    private int entryId;
    private String customerName;
    private String contactNumber;
    private int guests;
    private LocalDateTime entryTime;
    private int estimatedWaitTime;
    private String status;

    public WaitingListEntry(int entryId, String customerName, String contactNumber, int guests, LocalDateTime entryTime) {
        this.entryId = entryId;
        this.customerName = customerName;
        this.contactNumber = contactNumber;
        this.guests = guests;
        this.entryTime = entryTime;
        this.estimatedWaitTime = 0;
        this.status = "Waiting";
    }

    public boolean updateStatus(String status) {
        this.status = status;
        return true;
    }

    public boolean notifyCustomer() {
        System.out.println("Notifying customer " + customerName + " at " + contactNumber + ": Table available.");
        return true;
    }

    public Booking convertToBooking(String tableId) {
        // Simulate conversion to a booking
        Table table = new Table(Integer.parseInt(tableId.replace("T", "")), guests, "Location");
        return new Booking("B" + entryId, table, new Customer(0, customerName, contactNumber, ""), LocalDateTime.now(), guests, "");
    }

    public int getEntryId() {
        return entryId;
    }

    public int getGuests() {
        return guests;
    }

    public String getStatus() {
        return status;
    }

    public void setEstimatedWaitTime(int minutes) {
        this.estimatedWaitTime = minutes;
    }
}