package com.restaurant.state;

import com.restaurant.domain.Booking;
import com.restaurant.domain.BookingDetails;
import com.restaurant.domain.TimeSlot;

public class ConfirmedState implements BookingState {
    @Override
    public boolean modify(Booking booking, BookingDetails updatedDetails) {
        booking.setDateTime(updatedDetails.getDateTime());
        booking.setGuests(updatedDetails.getGuests());
        booking.setSpecialRequirements(updatedDetails.getSpecialRequirements());
        booking.setState(new ModifiedState());
        return true;
    }

    @Override
    public boolean cancel(Booking booking) {
        booking.setState(new CanceledState());
        booking.getTable().release(new TimeSlot(booking.getDateTime()));
        return true;
    }

    @Override
    public String getStateName() {
        return "Confirmed";
    }
}