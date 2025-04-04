package com.restaurant.state;

import com.restaurant.domain.Booking;
import com.restaurant.domain.BookingDetails;

public class CanceledState implements BookingState {
    @Override
    public boolean modify(Booking booking, BookingDetails updatedDetails) {
        System.out.println("Cannot modify a canceled booking.");
        return false;
    }

    @Override
    public boolean cancel(Booking booking) {
        System.out.println("Booking is already canceled.");
        return false;
    }

    @Override
    public String getStateName() {
        return "Canceled";
    }
}