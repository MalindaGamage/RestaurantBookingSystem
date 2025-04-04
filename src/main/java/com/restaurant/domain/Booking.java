package com.restaurant.domain;

import com.restaurant.state.BookingState;
import com.restaurant.state.ConfirmedState;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Booking {
    private String bookingRef;
    private Table table;
    private Customer customer;
    private LocalDateTime dateTime;
    private int guests;
    private String specialRequirements;
    private BookingState state;
    private List<BookingObserver> observers;

    public Booking(String bookingRef, Table table, Customer customer, LocalDateTime dateTime, int guests, String specialRequirements) {
        this.bookingRef = bookingRef;
        this.table = table;
        this.customer = customer;
        this.dateTime = dateTime;
        this.guests = guests;
        this.specialRequirements = specialRequirements;
        this.state = new ConfirmedState();
        this.observers = new ArrayList<>();
        table.reserve(new TimeSlot(dateTime));
    }

    public void addObserver(BookingObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(BookingObserver observer) {
        observers.remove(observer);
    }

    public void notifyObservers() {
        for (BookingObserver observer : observers) {
            observer.update(this);
        }
    }

    public boolean modify(BookingDetails updatedDetails) {
        return state.modify(this, updatedDetails);
    }

    public boolean cancel() {
        return state.cancel(this);
    }

    public boolean sendConfirmation(String method) {
        System.out.println("Sending confirmation via " + method + ": Booking " + bookingRef + " confirmed.");
        return true;
    }

    public String getDetails() {
        return "Booking ID: " + bookingRef + ", Date: " + dateTime.toLocalDate() + ", Time: " + dateTime.toLocalTime() +
                ", Guests: " + guests + ", Status: " + state.getStateName() + ", Table: " + table.getTableId();
    }

    public void setState(BookingState state) {
        this.state = state;
        notifyObservers();
    }

    public String getBookingRef() {
        return bookingRef;
    }

    public Table getTable() {
        return table;
    }

    public Customer getCustomer() {
        return customer;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public int getGuests() {
        return guests;
    }

    public void setGuests(int guests) {
        this.guests = guests;
    }

    public void setSpecialRequirements(String specialRequirements) {
        this.specialRequirements = specialRequirements;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public BookingState getState() {
        return state;
    }

    public String getSpecialRequirements() {
        return specialRequirements;
    }

}