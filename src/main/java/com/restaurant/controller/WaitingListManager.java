package com.restaurant.controller;

import com.restaurant.domain.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class WaitingListManager {
    private List<WaitingListEntry> waitingList;
    private int nextEntryId;

    public WaitingListManager() {
        this.waitingList = new ArrayList<>();
        this.nextEntryId = 1;
    }

    public boolean addToWaitingList(String customerName, int guests, String contactNumber, int estimatedWaitTime) {
        WaitingListEntry entry = new WaitingListEntry(nextEntryId++, customerName, contactNumber, guests, LocalDateTime.now());
        entry.setEstimatedWaitTime(estimatedWaitTime);
        return waitingList.add(entry);
    }

    public WaitingListEntry getNextCustomer() {
        if (waitingList.isEmpty()) return null;
        waitingList.sort(Comparator.comparing(WaitingListEntry::getEntryId)); // Priority by entry ID (FIFO)
        for (WaitingListEntry entry : waitingList) {
            if ("Waiting".equals(entry.getStatus())) {
                return entry; // Return first waiting customer
            }
        }
        return null; // No waiting customers
    }

    public void markAttemptedContact(int entryId) {
        for (WaitingListEntry entry : waitingList) {
            if (entry.getEntryId() == entryId) {
                entry.updateStatus("Attempted Contact");
                break;
            }
        }
    }

    public WaitingListEntry removeFromWaitingList(int entryId) {
        WaitingListEntry toRemove = null;
        for (WaitingListEntry entry : waitingList) {
            if (entry.getEntryId() == entryId) {
                toRemove = entry;
                break;
            }
        }
        if (toRemove != null) {
            waitingList.remove(toRemove);
            return toRemove;
        }
        return null;
    }

    public List<WaitingListEntry> getWaitingList() {
        return new ArrayList<>(waitingList); // Return a copy
    }

    public WaitingListEntry addCustomerToWaitingList(Customer customerInfo) {
        WaitingListEntry entry = new WaitingListEntry(
                waitingList.size() + 1, customerInfo.getName(), customerInfo.getPhone(), 2, LocalDateTime.now());
        entry.setEstimatedWaitTime(calculateEstimatedWaitTime());
        waitingList.add(entry);
        return entry;
    }

    public int calculateEstimatedWaitTime() {
        return waitingList.size() * 10; // 10 minutes per entry
    }

    public boolean notifyWhenTableAvailable(int entryId) {
        for (WaitingListEntry entry : waitingList) {
            if (entry.getEntryId() == entryId) {
                return entry.notifyCustomer();
            }
        }
        return false;
    }

    public void manageWaitingList() {
        WaitingListEntry next = getNextCustomer();
        if (next != null) {
            next.notifyCustomer();
        }
    }
}