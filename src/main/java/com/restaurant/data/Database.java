package com.restaurant.data;

import com.restaurant.domain.Booking;
import com.restaurant.domain.WaitingListEntry;

public class Database {
    public static void saveBooking(Booking booking) {
        System.out.println("Saving booking: " + booking.getDetails());
    }

    public static void updateTableStatus(String tableId, String status) {
        System.out.println("Updating table " + tableId + " status to " + status);
    }

    public static void saveWaitingListEntry(WaitingListEntry entry) {
        System.out.println("Saving waiting list entry: " + entry.getEntryId());
    }
}