package com.restaurant.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;

public class Util {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    private static final Pattern PHONE_PATTERN = Pattern.compile("\\d{10}"); // 10-digit phone number
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$"); // Basic email format

    // Validate date in YYYY-MM-DD format and ensure it's in the future
    public static LocalDateTime validateDate(String date, String time) {
        try {
            LocalDateTime dateTime = LocalDateTime.parse(date + "T" + time, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));
            if (dateTime.isBefore(LocalDateTime.now())) {
                throw new IllegalArgumentException("Date and time must be in the future.");
            }
            return dateTime;
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date or time format. Use YYYY-MM-DD for date and HH:MM for time.");
        }
    }

    // Validate number of guests (must be positive and reasonable, e.g., <= 20)
    public static int validateGuests(int guests) {
        if (guests <= 0 || guests > 20) {
            throw new IllegalArgumentException("Number of guests must be between 1 and 20.");
        }
        return guests;
    }

    // Validate customer name (non-empty, letters and spaces only)
    public static String validateCustomerName(String name) {
        if (name == null || name.trim().isEmpty() || !name.matches("[A-Za-z\\s]+")) {
            throw new IllegalArgumentException("Customer name must contain only letters and spaces and cannot be empty.");
        }
        return name.trim();
    }

    // Validate phone number (10 digits)
    public static String validatePhone(String phone) {
        if (phone == null || !PHONE_PATTERN.matcher(phone).matches()) {
            throw new IllegalArgumentException("Phone number must be a 10-digit number.");
        }
        return phone;
    }

    // Validate email (basic email format)
    public static String validateEmail(String email) {
        if (email == null || !EMAIL_PATTERN.matcher(email).matches()) {
            throw new IllegalArgumentException("Invalid email format.");
        }
        return email;
    }

    // Validate special requirements (non-null, max length 100)
    public static String validateSpecialRequirements(String requirements) {
        if (requirements == null) {
            return "";
        }
        if (requirements.length() > 100) {
            throw new IllegalArgumentException("Special requirements cannot exceed 100 characters.");
        }
        return requirements;
    }

    // Validate booking reference (non-empty)
    public static String validateBookingRef(String bookingRef) {
        if (bookingRef == null || bookingRef.trim().isEmpty()) {
            throw new IllegalArgumentException("Booking reference cannot be empty.");
        }
        return bookingRef.trim();
    }

    // Validate table ID (format T followed by a number, e.g., T1)
    public static String validateTableId(String tableId) {
        if (tableId == null || !tableId.matches("T\\d+")) {
            throw new IllegalArgumentException("Table ID must be in the format T followed by a number (e.g., T1).");
        }
        return tableId;
    }

    // Validate table status (must be one of the allowed values)
    public static String validateTableStatus(String status) {
        String[] validStatuses = {"Available", "Reserved", "Occupied", "Needs Cleaning"};
        for (String validStatus : validStatuses) {
            if (validStatus.equalsIgnoreCase(status)) {
                return validStatus;
            }
        }
        throw new IllegalArgumentException("Invalid table status. Must be one of: Available, Reserved, Occupied, Needs Cleaning.");
    }

    // Validate report type
    public static String validateReportType(String type) {
        String[] validTypes = {"Booking", "Occupancy", "Revenue"};
        for (String validType : validTypes) {
            if (validType.equalsIgnoreCase(type)) {
                return validType;
            }
        }
        throw new IllegalArgumentException("Invalid report type. Must be one of: Booking, Occupancy, Revenue.");
    }

    // Validate export format
    public static String validateExportFormat(String format) {
        String[] validFormats = {"PDF", "CSV"};
        for (String validFormat : validFormats) {
            if (validFormat.equalsIgnoreCase(format)) {
                return validFormat;
            }
        }
        throw new IllegalArgumentException("Invalid export format. Must be one of: PDF, CSV.");
    }

    // Validate notification method
    public static String validateNotificationMethod(String method) {
        String[] validMethods = {"Email", "SMS"};
        for (String validMethod : validMethods) {
            if (validMethod.equalsIgnoreCase(method)) {
                return validMethod;
            }
        }
        throw new IllegalArgumentException("Invalid notification method. Must be one of: Email, SMS.");
    }

    // Validate customer ID (positive integer)
    public static int validateCustomerId(int customerId) {
        if (customerId <= 0) {
            throw new IllegalArgumentException("Customer ID must be a positive integer.");
        }
        return customerId;
    }
}