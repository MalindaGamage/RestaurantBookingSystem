package com.restaurant.domain;

import com.restaurant.controller.BookingController;
import com.restaurant.controller.ReportController;
import com.restaurant.domain.Table;
import com.restaurant.domain.Booking;
import com.restaurant.domain.Customer;
import com.restaurant.domain.BookingDetails;
import com.restaurant.domain.WalkinRecord;

public class Staff extends User {
    private int staffId;
    private String name;
    private String contactInfo;
    private final BookingController bookingController;
    private final ReportController reportController;

    public Staff(int userId, String username, String password, String role, int staffId, String name, String contactInfo,
                 BookingController bookingController, ReportController reportController) {
        super(userId, username, password, role);
        this.staffId = staffId;
        this.name = name;
        this.contactInfo = contactInfo;
        this.bookingController = bookingController;
        this.reportController = reportController;
    }

    public Table[] checkTableAvailability(String date, String time, int guests) {
        return bookingController.checkAvailability(date, time, guests); // UC1
    }

    public Booking createBooking(String tableId, Customer customerInfo, BookingDetails bookingDetails) {
        return bookingController.createBooking(tableId, customerInfo, bookingDetails); // UC2
    }

    public boolean modifyBooking(String bookingRef, BookingDetails updatedDetails) {
        return bookingController.updateBooking(bookingRef, updatedDetails); // UC3
    }

    public boolean cancelBooking(String bookingRef) {
        return bookingController.deleteBooking(bookingRef); // UC4
    }

    public WalkinRecord recordWalkin(String tableId, Customer customerInfo) {
        return bookingController.recordWalkin(tableId, customerInfo); // UC5
    }

    public boolean updateTableStatus(String tableId, String status) {
        return bookingController.updateTableStatus(tableId, status); // UC7
    }

    public void manageWaitingList() {
        bookingController.manageWaitingList(); // UC8
    }

    public Customer viewCustomerHistory(int customerId) {
        return bookingController.viewCustomerHistory(customerId); // UC10
    }
}