package com.restaurant.domain;

import java.time.LocalDateTime;

public class TimeSlot {
    private LocalDateTime startTime;

    public TimeSlot(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }
}