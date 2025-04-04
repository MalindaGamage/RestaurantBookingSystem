package com.restaurant.controller;

import com.restaurant.domain.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class WaitingListManager {
    private List<WaitingListEntry> waitingList;

    public WaitingListManager() {
        this.waitingList = new ArrayList<>();
    }

    public WaitingListEntry addCustomerToWaitingList(Customer customerInfo) {
        WaitingListEntry entry = new WaitingListEntry(
                waitingList.size() + 1, customerInfo.getName(), customerInfo.getPhone(), 2, LocalDateTime.now());
        entry.setEstimatedWaitTime(calculateEstimatedWaitTime());
        waitingList.add(entry);
        return entry;
    }

    public boolean removeFromWaitingList(int entryId) {
        return waitingList.removeIf(entry -> entry.getEntryId() == entryId);
    }

    public WaitingListEntry getNextInLine() {
        if (waitingList.isEmpty()) return null;
        for (WaitingListEntry entry : waitingList) {
            if (entry.getStatus().equals("Waiting")) {
                return entry;
            }
        }
        return null;
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
        WaitingListEntry next = getNextInLine();
        if (next != null) {
            next.notifyCustomer();
        }
    }
}