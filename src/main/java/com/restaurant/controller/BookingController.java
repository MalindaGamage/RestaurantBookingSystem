package com.restaurant.controller;

import com.restaurant.domain.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class BookingController {
    private TableManager tableManager;
    protected BookingManager bookingManager;
    private WaitingListManager waitingListManager;
    private SystemSettingsManager settingsManager;

    public BookingController() {
        this.tableManager = new TableManager();
        this.bookingManager = new BookingManager();
        this.waitingListManager = new WaitingListManager();
        this.settingsManager = new SystemSettingsManager();
    }

    public Table[] checkAvailability(String date, String time, int guests) {
        LocalDateTime dateTime = LocalDateTime.parse(date + "T" + time);
        return tableManager.findAvailableTables(date, time, guests); // UC1
    }

    public Booking createBooking(String tableId, Customer customerInfo, BookingDetails bookingDetails) {
        Booking booking = bookingManager.registerBooking(tableId, customerInfo, bookingDetails); // UC2
        if (booking != null) {
            BookingSystem.getInstance().getBookings().add(booking);
            Report report = new Report("R" + System.currentTimeMillis(), "Booking", "All");
            booking.addObserver(report);
        }
        return booking;
    }

    public Booking findBooking(String referenceOrName) {
        return bookingManager.retrieveBooking(referenceOrName); // Used in UC3, UC4
    }

    public boolean updateBooking(String bookingRef, BookingDetails updatedDetails) {
        return bookingManager.modifyBooking(bookingRef, updatedDetails); // UC3
    }

    public boolean deleteBooking(String bookingRef) {
        return bookingManager.cancelBooking(bookingRef); // UC4
    }

    public WalkinRecord recordWalkin(String tableId, Customer customerInfo, int guests) {
        Table[] availableTables = getCurrentAvailability(guests); // Assume Customer has getGuests()
        if (availableTables.length == 0) {
            return null; // No tables available
        }
        WalkinRecord walkin = bookingManager.createWalkinRecord(tableId, customerInfo); // UC5
        tableManager.updateTableStatus(tableId, "Occupied", LocalDateTime.now());
        return walkin;
    }

    public Table[] getCurrentAvailability(int guests) {
        return tableManager.findCurrentlyAvailableTables(guests); // UC5
    }

    public boolean requestConfirmation(String bookingRef, String method) {
        Booking booking = findBooking(bookingRef);
        if (booking != null) {
            return booking.sendConfirmation(method); // UC9
        }
        return false;
    }

    public void manageWaitingList() {
        waitingListManager.manageWaitingList(); // UC8
    }

    public Customer viewCustomerHistory(int customerId) {
        List<Booking> bookings = BookingSystem.getInstance().getBookings();
        if (bookings == null || bookings.isEmpty()) {
            System.out.println("No bookings found in the system.");
            return null;
        }
        Customer foundCustomer = null;
        for (Booking booking : bookings) {
            Customer customer = booking.getCustomer();
            if (customer != null && customer.getCustomerId() == customerId) {
                if (foundCustomer == null) {
                    foundCustomer = customer;
                }
                // Add the booking to the customer's history
                foundCustomer.addToHistory(booking);
            }
        }

        if (foundCustomer == null) {
            System.out.println("Customer with ID " + customerId + " not found.");
        }
        return foundCustomer; // UC10
    }

    public boolean updateTableStatus(String tableId, String status) {
        return tableManager.updateTableStatus(tableId, status, LocalDateTime.now()); // UC7
    }

    public boolean updateTableStatuses(List<String> tableIds, String status) {
        return tableManager.updateTableStatuses(tableIds, status, LocalDateTime.now());
    }

    public boolean addToWaitingList(String customerName, int guests, String contactNumber, int estimatedWaitTime) {
        return waitingListManager.addToWaitingList(customerName, guests, contactNumber, estimatedWaitTime);
    }

    public WaitingListEntry getNextWaitingCustomer() {
        return waitingListManager.getNextCustomer();
    }

    public void markAttemptedContact(int entryId) {
        waitingListManager.markAttemptedContact(entryId);
    }

    public Booking convertWaitingToBooking(int entryId, String tableId) {
        WaitingListEntry entry = waitingListManager.removeFromWaitingList(entryId);
        if (entry != null && entry.notifyCustomer()) {
            return entry.convertToBooking(tableId);
        }
        return null;
    }

    public boolean updateSystemSettings(Map<String, Object> settings) {
        return settingsManager.updateSettings(settings); // UC11
    }
}