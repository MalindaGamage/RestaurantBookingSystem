package com.restaurant.domain;

import java.time.LocalDateTime;

public class BookingDetails {
    private LocalDateTime dateTime;
    private int guests;
    private String specialRequirements;

    public BookingDetails(LocalDateTime dateTime, int guests, String specialRequirements) {
        this.dateTime = dateTime;
        this.guests = guests;
        this.specialRequirements = specialRequirements;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public int getGuests() {
        return guests;
    }

    public String getSpecialRequirements() {
        return specialRequirements;
    }
}