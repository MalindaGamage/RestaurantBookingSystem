package com.restaurant.strategy;

import com.restaurant.domain.Table;

import java.util.List;

public class OptimalCapacityStrategy implements TableAssignmentStrategy {
    @Override
    public Table[] assignTables(List<Table> availableTables, int guests) {
        // Find the table with the closest capacity to the number of guests
        Table bestFit = null;
        int minDiff = Integer.MAX_VALUE;
        for (Table table : availableTables) {
            int diff = table.getCapacity() - guests;
            if (diff >= 0 && diff < minDiff) {
                minDiff = diff;
                bestFit = table;
            }
        }
        return bestFit != null ? new Table[]{bestFit} : new Table[0];
    }
}