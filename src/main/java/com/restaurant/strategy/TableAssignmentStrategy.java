package com.restaurant.strategy;

import com.restaurant.domain.Table;

import java.util.List;

public interface TableAssignmentStrategy {
    Table[] assignTables(List<Table> availableTables, int guests);
}