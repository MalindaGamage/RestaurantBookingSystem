package com.restaurant.state;

import com.restaurant.domain.Booking;
import com.restaurant.domain.BookingDetails;

public interface BookingState {
    boolean modify(Booking booking, BookingDetails updatedDetails);
    boolean cancel(Booking booking);
    String getStateName();
}