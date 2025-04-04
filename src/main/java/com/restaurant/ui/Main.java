package com.restaurant.ui;

import com.restaurant.controller.*;
import com.restaurant.domain.*;
import com.restaurant.strategy.*;
import com.restaurant.util.Util;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        BookingSystem system = BookingSystem.getInstance();
        system.initialize();

        // Create a staff member and manager with dependency injection
        Staff staff = new Staff(1, "staff1", "password", "Staff", 1, "John Doe", "john@example.com",
                system.getBookingController(), system.getReportController());
        Manager manager = new Manager(2, "manager1", "password", "Manager", 2, "Jane Doe", "jane@example.com", 1,
                system.getBookingController(), system.getReportController());

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\nRestaurant Table Booking System");
            System.out.println("1. Check Table Availability (UC1)");
            System.out.println("2. Create Booking (UC2)");
            System.out.println("3. Modify Booking (UC3)");
            System.out.println("4. Cancel Booking (UC4)");
            System.out.println("5. Record Walk-in (UC5)");
            System.out.println("6. Generate Reports (UC6)");
            System.out.println("7. Update Table Status (UC7)");
            System.out.println("8. Manage Waiting List (UC8)");
            System.out.println("9. Send Confirmation (UC9)");
            System.out.println("10. View Customer History (UC10)");
            System.out.println("11. Manage System Settings (UC11)");
            System.out.println("12. Exit");
            System.out.print("Choose an option: ");

            int choice;
            try {
                choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a number between 1 and 12.");
                scanner.nextLine(); // Clear invalid input
                continue;
            }

            switch (choice) {
                case 1: // UC1: Check Table Availability
                    try {
                        System.out.print("Enter date (YYYY-MM-DD): ");
                        String date = scanner.nextLine();
                        System.out.print("Enter time (HH:MM): ");
                        String time = scanner.nextLine();
                        Util.validateDate(date, time); // Validate date and time
                        System.out.print("Enter number of guests: ");
                        int guests = Util.validateGuests(scanner.nextInt());
                        scanner.nextLine();

                        Table[] availableTables = staff.checkTableAvailability(date, time, guests);
                        System.out.println("Available Tables:");
                        if (availableTables.length == 0) {
                            System.out.println("No tables available.");
                        } else {
                            for (Table table : availableTables) {
                                System.out.println("Table ID: " + table.getTableId() + ", Capacity: " + table.getCapacity());
                            }
                        }
                    } catch (IllegalArgumentException e) {
                        System.out.println("Error: " + e.getMessage());
                    } catch (Exception e) {
                        System.out.println("Unexpected error: " + e.getMessage());
                        scanner.nextLine(); // Clear invalid input
                    }
                    break;

                case 2: // UC2: Create Booking
                    try {
                        System.out.print("Enter date (YYYY-MM-DD): ");
                        String date = scanner.nextLine();
                        System.out.print("Enter time (HH:MM): ");
                        String time = scanner.nextLine();
                        LocalDateTime dateTime = Util.validateDate(date, time);
                        System.out.print("Enter number of guests: ");
                        int guests = Util.validateGuests(scanner.nextInt());
                        scanner.nextLine();
                        System.out.print("Enter customer name: ");
                        String customerName = Util.validateCustomerName(scanner.nextLine());
                        System.out.print("Enter customer phone: ");
                        String customerPhone = Util.validatePhone(scanner.nextLine());
                        System.out.print("Enter customer email: ");
                        String customerEmail = Util.validateEmail(scanner.nextLine());
                        System.out.print("Enter special requirements: ");
                        String specialRequirements = Util.validateSpecialRequirements(scanner.nextLine());

                        Customer customer = new Customer(1, customerName, customerPhone, customerEmail);
                        Table[] availableTables = staff.checkTableAvailability(date, time, guests);
                        if (availableTables.length == 0) {
                            System.out.println("No tables available. Consider adding to waiting list (UC8).");
                            break;
                        }
                        System.out.println("Available Table: " + availableTables[0].getTableId());
                        BookingDetails bookingDetails = new BookingDetails(dateTime, guests, specialRequirements);
                        Booking booking = staff.createBooking(availableTables[0].getTableId(), customer, bookingDetails);
                        if (booking != null) {
                            System.out.println("Booking created: " + booking.getDetails());
                            customer.addBooking(booking);
                        } else {
                            System.out.println("Failed to create booking.");
                        }
                    } catch (IllegalArgumentException e) {
                        System.out.println("Error: " + e.getMessage());
                    } catch (Exception e) {
                        System.out.println("Unexpected error: " + e.getMessage());
                        scanner.nextLine(); // Clear invalid input
                    }
                    break;

                case 3: // UC3: Modify Booking
                    try {
                        System.out.print("Enter booking reference: ");
                        String bookingRef = Util.validateBookingRef(scanner.nextLine());
                        System.out.print("Enter new date (YYYY-MM-DD): ");
                        String date = scanner.nextLine();
                        System.out.print("Enter new time (HH:MM): ");
                        String time = scanner.nextLine();
                        LocalDateTime dateTime = Util.validateDate(date, time);
                        System.out.print("Enter new number of guests: ");
                        int guests = Util.validateGuests(scanner.nextInt());
                        scanner.nextLine();

                        BookingDetails bookingDetails = new BookingDetails(dateTime, guests, "");
                        boolean modified = staff.modifyBooking(bookingRef, bookingDetails);
                        if (modified) {
                            System.out.println("Booking modified successfully.");
                        } else {
                            System.out.println("Failed to modify booking.");
                        }
                    } catch (IllegalArgumentException e) {
                        System.out.println("Error: " + e.getMessage());
                    } catch (Exception e) {
                        System.out.println("Unexpected error: " + e.getMessage());
                        scanner.nextLine(); // Clear invalid input
                    }
                    break;

                case 4: // UC4: Cancel Booking
                    try {
                        System.out.print("Enter booking reference: ");
                        String bookingRef = Util.validateBookingRef(scanner.nextLine());
                        boolean canceled = staff.cancelBooking(bookingRef);
                        if (canceled) {
                            System.out.println("Booking canceled successfully.");
                        } else {
                            System.out.println("Failed to cancel booking.");
                        }
                    } catch (IllegalArgumentException e) {
                        System.out.println("Error: " + e.getMessage());
                    } catch (Exception e) {
                        System.out.println("Unexpected error: " + e.getMessage());
                    }
                    break;

                case 5: // UC5: Record Walk-in
                    try {
                        System.out.print("Enter number of guests: ");
                        int guests = Util.validateGuests(scanner.nextInt());
                        scanner.nextLine();
                        System.out.print("Enter customer name: ");
                        String customerName = Util.validateCustomerName(scanner.nextLine());
                        Customer customer = new Customer(2, customerName, "", "");
                        Table[] currentTables = system.getBookingController().getCurrentAvailability(guests);
                        if (currentTables.length == 0) {
                            System.out.println("No tables available. Consider adding to waiting list (UC8).");
                            break;
                        }
                        WalkinRecord walkin = staff.recordWalkin(currentTables[0].getTableId(), customer, guests);
                        System.out.println("Walk-in recorded: Record ID " + walkin.getRecordId() + ", Table " + walkin.getTable().getTableId());
                    } catch (IllegalArgumentException e) {
                        System.out.println("Error: " + e.getMessage());
                    } catch (Exception e) {
                        System.out.println("Unexpected error: " + e.getMessage());
                        scanner.nextLine(); // Clear invalid input
                    }
                    break;

                case 6: // UC6: Generate Reports
                    try {
                        System.out.print("Enter report type (Booking/Occupancy/Revenue): ");
                        String type = Util.validateReportType(scanner.nextLine());
                        System.out.print("Enter parameters: ");
                        String parameters = scanner.nextLine();
                        Report report = manager.generateReports(type, parameters);
                        report.display();
                        System.out.print("Export report? (yes/no): ");
                        if (scanner.nextLine().equalsIgnoreCase("yes")) {
                            System.out.print("Enter format (PDF/CSV): ");
                            String format = Util.validateExportFormat(scanner.nextLine());
                            System.out.println(system.getReportController().requestExport(report, format));
                        }
                    } catch (IllegalArgumentException e) {
                        System.out.println("Error: " + e.getMessage());
                    } catch (Exception e) {
                        System.out.println("Unexpected error: " + e.getMessage());
                    }
                    break;

                case 7: // UC7: Update Table Status
                    try {
                        System.out.print("Enter table ID (e.g., T1): ");
                        String tableId = Util.validateTableId(scanner.nextLine());
                        System.out.print("Enter new status (Available/Reserved/Occupied/Needs Cleaning): ");
                        String status = Util.validateTableStatus(scanner.nextLine());
                        boolean updated = staff.updateTableStatus(tableId, status);
                        if (updated) {
                            System.out.println("Table status updated to " + status);
                        } else {
                            System.out.println("Failed to update table status.");
                        }
                    } catch (IllegalArgumentException e) {
                        System.out.println("Error: " + e.getMessage());
                    } catch (Exception e) {
                        System.out.println("Unexpected error: " + e.getMessage());
                    }
                    break;

                case 8: // UC8: Manage Waiting List
                    try {
                        System.out.print("Enter customer name: ");
                        String customerName = Util.validateCustomerName(scanner.nextLine());
                        System.out.print("Enter contact number: ");
                        String contactNumber = Util.validatePhone(scanner.nextLine());
                        System.out.print("Enter number of guests: ");
                        int guests = Util.validateGuests(scanner.nextInt());
                        scanner.nextLine();
                        Customer customer = new Customer(3, customerName, contactNumber, "");
                        WaitingListEntry entry = new WaitingListManager().addCustomerToWaitingList(customer);
                        System.out.println("Added to waiting list: Entry ID " + entry.getEntryId());
                        staff.manageWaitingList();
                    } catch (IllegalArgumentException e) {
                        System.out.println("Error: " + e.getMessage());
                    } catch (Exception e) {
                        System.out.println("Unexpected error: " + e.getMessage());
                        scanner.nextLine(); // Clear invalid input
                    }
                    break;

                case 9: // UC9: Send Confirmation
                    try {
                        System.out.print("Enter booking reference: ");
                        String bookingRef = Util.validateBookingRef(scanner.nextLine());
                        System.out.print("Enter method (Email/SMS): ");
                        String method = Util.validateNotificationMethod(scanner.nextLine());
                        boolean sent = system.getBookingController().requestConfirmation(bookingRef, method);
                        if (sent) {
                            System.out.println("Confirmation sent successfully.");
                        } else {
                            System.out.println("Failed to send confirmation.");
                        }
                    } catch (IllegalArgumentException e) {
                        System.out.println("Error: " + e.getMessage());
                    } catch (Exception e) {
                        System.out.println("Unexpected error: " + e.getMessage());
                    }
                    break;

                case 10: // UC10: View Customer History
                    try {
                        System.out.print("Enter customer ID: ");
                        int customerId = Util.validateCustomerId(scanner.nextInt());
                        scanner.nextLine();
                        Customer customerHistory = staff.viewCustomerHistory(customerId);
                        if (customerHistory != null) {
                            System.out.println("Customer History for " + customerHistory.getName() + ":");
                            for (Booking b : customerHistory.getBookingHistory()) {
                                System.out.println(b.getDetails());
                            }
                        } else {
                            System.out.println("Customer not found.");
                        }
                    } catch (IllegalArgumentException e) {
                        System.out.println("Error: " + e.getMessage());
                    } catch (Exception e) {
                        System.out.println("Unexpected error: " + e.getMessage());
                        scanner.nextLine(); // Clear invalid input
                    }
                    break;

                case 11: // UC11: Manage System Settings
                    try {
                        manager.manageSystemSettings(); // Step 1: Access settings
                        System.out.println("Manage System Settings (Enter 'exit' to cancel):");

                        Map<String, Object> settings = new HashMap<>();
                        boolean settingsValid = false;

                        while (!settingsValid) {
                            // Collect operating hours
                            System.out.print("Enter operating hours (HH:MM-HH:MM, e.g., 09:00-22:00): ");
                            String hoursInput = scanner.nextLine();
                            if (hoursInput.equalsIgnoreCase("exit")) {
                                System.out.println("Settings update canceled.");
                                break;
                            }
                            try {
                                if (!hoursInput.matches("\\d{2}:\\d{2}-\\d{2}:\\d{2}")) {
                                    throw new IllegalArgumentException("Invalid format. Use HH:MM-HH:MM (e.g., 09:00-22:00).");
                                }
                                settings.put("operatingHours", hoursInput);
                            } catch (IllegalArgumentException e) {
                                System.out.println("Error: " + e.getMessage());
                                continue; // Retry input
                            }

                            // Collect table count
                            System.out.print("Enter number of tables (positive integer): ");
                            String tableCountInput = scanner.nextLine();
                            if (tableCountInput.equalsIgnoreCase("exit")) {
                                System.out.println("Settings update canceled.");
                                break;
                            }
                            try {
                                int tableCount = Integer.parseInt(tableCountInput);
                                if (tableCount < 0) {
                                    throw new IllegalArgumentException("Number of tables must be non-negative.");
                                }
                                settings.put("tableCount", tableCount);
                            } catch (NumberFormatException e) {
                                System.out.println("Error: Please enter a valid integer.");
                                continue; // Retry input
                            } catch (IllegalArgumentException e) {
                                System.out.println("Error: " + e.getMessage());
                                continue; // Retry input
                            }

                            // Attempt to update settings
                            settingsValid = system.getBookingController().updateSystemSettings(settings);
                            if (!settingsValid) {
                                System.out.println("Invalid settings detected. Please correct and retry.");
                                settings.clear(); // Reset for retry
                            }
                        }

                        if (settingsValid) {
                            System.out.println("System settings updated successfully.");
                        }
                    } catch (Exception e) {
                        System.out.println("Unexpected error: " + e.getMessage());
                    }
                    break;

                case 12: // Exit
                    System.out.println("Exiting system...");
                    scanner.close();
                    return;

                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }
}