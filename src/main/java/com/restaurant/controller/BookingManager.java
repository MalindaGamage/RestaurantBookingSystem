package com.restaurant.controller;

import com.restaurant.domain.*;
import com.restaurant.state.CanceledState;

import java.time.LocalDateTime;

public class BookingManager {
    private BookingFactory bookingFactory;

    public BookingManager() {
        this.bookingFactory = new BookingFactory();
    }

    public Booking registerBooking(String tableId, Customer customerInfo, BookingDetails bookingDetails) {
        for (Booking existing : BookingSystem.getInstance().getBookings()) {
            if (existing.getTable().getTableId().equals(tableId) &&
                    existing.getDateTime().equals(bookingDetails.getDateTime())) {
                return null; // Duplicate detected
            }
        }
        return bookingFactory.createBooking(tableId, customerInfo, bookingDetails);
    }

    public Booking retrieveBooking(String referenceOrName) {
        for (Booking booking : BookingSystem.getInstance().getBookings()) {
            if (booking.getBookingRef().equals(referenceOrName) ||
                    booking.getCustomer().getName().equalsIgnoreCase(referenceOrName)) {
                return booking;
            }
        }
        return null;
    }

    public boolean modifyBooking(String bookingRef, BookingDetails updatedDetails) {
        Booking booking = retrieveBooking(bookingRef);
        if (booking != null) {
            return booking.modify(updatedDetails);
        }
        return false;
    }

    public boolean cancelBooking(String bookingRef) {
        Booking booking = retrieveBooking(bookingRef);
        if (booking != null) {
            booking.setState(new CanceledState());  // Explicitly set the state
            return true;
        }
        return false;
    }


    public WalkinRecord createWalkinRecord(String tableId, Customer customerInfo) {
        return new WalkinRecord("W" + System.currentTimeMillis(),
                findTableById(tableId), customerInfo.getName(), 2, LocalDateTime.now());
    }

    public String generateConfirmation(String bookingRef) {
        Booking booking = retrieveBooking(bookingRef);
        if (booking != null) {
            return booking.getDetails();
        }
        return "Booking not found.";
    }

    public Booking[] searchBookings(String criteria) {
        return BookingSystem.getInstance().getBookings().toArray(new Booking[0]);
    }

    private Table findTableById(String tableId) {
        for (Table table : BookingSystem.getInstance().getTables()) {
            if (table.getTableId().equals(tableId)) {
                return table;
            }
        }
        return null;
    }
}