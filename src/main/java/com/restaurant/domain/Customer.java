package com.restaurant.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Customer {
    private int customerId;
    private String name;
    private String phone;
    private String email;
    private String preferences;
    private List<Booking> bookingHistory;

    public Customer(int customerId, String name, String phone, String email) {
        this.customerId = customerId;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.preferences = "";
        this.bookingHistory = new ArrayList<>();
    }

    public void addToHistory(Booking booking) {
        this.bookingHistory.add(booking);
    }

    public void addBooking(Booking booking) {
        bookingHistory.add(booking);
    }

    public boolean updateDetails(String preferences) {
        this.preferences = preferences;
        return true;
    }

    public List<Booking> getBookingHistory() {
        return Collections.unmodifiableList(bookingHistory);
    }

    public int getCustomerId() {
        return customerId;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getGuests() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public List<Booking> getHistory() {
        return new ArrayList<>(bookingHistory); // Return a copy
    }
}