package com.restaurant.strategy;

import com.restaurant.domain.Table;

import java.util.ArrayList;
import java.util.List;

public class MinimumTablesStrategy implements TableAssignmentStrategy {
    @Override
    public Table[] assignTables(List<Table> availableTables, int guests) {
        // Use the smallest number of tables possible
        List<Table> selectedTables = new ArrayList<>();
        int remainingGuests = guests;
        for (Table table : availableTables) {
            if (remainingGuests <= 0) break;
            if (table.getCapacity() >= remainingGuests) {
                selectedTables.add(table);
                break;
            } else {
                selectedTables.add(table);
                remainingGuests -= table.getCapacity();
            }
        }
        return selectedTables.toArray(new Table[0]);
    }
}