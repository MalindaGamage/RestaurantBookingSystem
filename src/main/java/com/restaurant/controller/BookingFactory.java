package com.restaurant.controller;

import com.restaurant.domain.*;
import java.time.LocalDateTime;

public class BookingFactory {
    public Booking createBooking(String tableId, Customer customerInfo, BookingDetails bookingDetails) {
        Table table = findTableById(tableId);
        if (table == null || !table.isAvailable(new TimeSlot(bookingDetails.getDateTime()))) {
            return null;
        }
        String bookingRef = "B" + System.currentTimeMillis();
        return new Booking(bookingRef, table, customerInfo, bookingDetails.getDateTime(),
                bookingDetails.getGuests(), bookingDetails.getSpecialRequirements());
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