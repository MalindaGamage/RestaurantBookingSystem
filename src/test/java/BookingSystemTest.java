import com.restaurant.controller.*;
import com.restaurant.domain.*;
import com.restaurant.state.CanceledState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BookingSystemTest {
    @Mock
    private TableManager tableManager;

    @Mock
    private BookingManager bookingManager;

    @Mock
    private WaitingListManager waitingListManager;

    @Mock
    private ReportManager reportManager;

    @Mock
    private SystemSettingsManager settingsManager;

    @Mock
    private BookingSystem bookingSystem;

    @InjectMocks
    private BookingController bookingController;

    @InjectMocks
    private ReportController reportController;

    @BeforeEach
    public void setUp() {
        BookingSystem.getInstance().getBookings().clear();
        MockitoAnnotations.openMocks(this);

        // Mock BookingSystem to return a list of bookings
//        List<Booking> bookings = new ArrayList<>();
//        Customer customer = new Customer(1, "Malinda Gamage", "973727398V", "pkgmalinda@gmail.com");
//        Table table = new Table(1, 4, "Window");
//        Booking booking = new Booking("B1", table, customer, LocalDateTime.now(), 4, "");
//        bookings.add(booking);
//        when(bookingSystem.getBookings()).thenReturn(bookings);
    }

    @Test
    public void testCreateBooking_TableNotAvailable() {
        // Arrange
        Customer customer = new Customer(1, "Malinda Gamage", "973727398V", "pkgmalinda@gmail.com");
        BookingDetails bookingDetails = new BookingDetails(LocalDateTime.of(2025, 4, 4, 18, 0), 4, "Window seat");
        when(bookingManager.registerBooking("T1", customer, bookingDetails)).thenReturn(null);

        // Act
        Booking result = bookingController.createBooking("T1", customer, bookingDetails);

        // Assert
        assertNull(result);
    }

    @Test
    public void testModifyBooking_BookingNotFound() {
        // Arrange
        when(bookingManager.retrieveBooking("B999")).thenReturn(null);

        BookingDetails updatedDetails = new BookingDetails(LocalDateTime.of(2025, 4, 5, 19, 0), 5, "");

        // Act
        boolean result = bookingController.updateBooking("B999", updatedDetails);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testModifyBooking_CanceledBooking() {
        // Arrange
        Booking booking = new Booking("B1", new Table(1, 4, "Window"),
                new Customer(1, "Malinda Gamage", "973727398V", "pkgmalinda@gmail.com"),
                LocalDateTime.of(2025, 4, 4, 18, 0), 4, "");
        booking.setState(new CanceledState());
        when(bookingManager.retrieveBooking("B1")).thenReturn(booking);

        BookingDetails updatedDetails = new BookingDetails(LocalDateTime.of(2025, 4, 5, 19, 0), 5, "");

        // Act
        boolean result = bookingController.updateBooking("B1", updatedDetails);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testCancelBooking_BookingNotFound() {
        // Arrange
        when(bookingManager.retrieveBooking("B999")).thenReturn(null);

        // Act
        boolean result = bookingController.deleteBooking("B999");

        // Assert
        assertFalse(result);
    }

    @Test
    public void testCancelBooking_AlreadyCanceled() {
        // Arrange
        Booking booking = new Booking("B1", new Table(1, 4, "Window"),
                new Customer(1, "Malinda Gamage", "973727398V", "pkgmalinda@gmail.com"),
                LocalDateTime.of(2025, 4, 4, 18, 0), 4, "");
        booking.setState(new CanceledState());
        when(bookingManager.retrieveBooking("B1")).thenReturn(booking);
        when(bookingManager.cancelBooking("B1")).thenReturn(false);

        // Act
        boolean result = bookingController.deleteBooking("B1");

        // Assert
        assertFalse(result);
    }

    @Test
    public void testGenerateReports_EmptyData() {
        // Arrange
        when(bookingSystem.getBookings()).thenReturn(new ArrayList<>());
        Report report = new Report("R1", "Booking", "All");
        when(reportManager.createReport("Booking", "All")).thenReturn(report);

        // Act
        Report result = reportController.generateReport("Booking", "All");

        // Assert
        assertNotNull(result);
        assertTrue(result.getData().isEmpty());
    }

    @Test
    public void testUpdateTableStatus_InvalidTableId() {
        // Arrange
        when(tableManager.updateTableStatus(eq("T999"), eq("Needs Cleaning"), any(LocalDateTime.class))).thenReturn(false);

        // Act
        boolean result = bookingController.updateTableStatus("T999", "Needs Cleaning");

        // Assert
        assertFalse(result);
    }

    @Test
    public void testManageWaitingList_EmptyList() {
        // Arrange
        when(waitingListManager.getNextCustomer()).thenReturn(null);

        // Act
        bookingController.manageWaitingList();

        // Assert
        verify(waitingListManager, times(1)).manageWaitingList();
    }

    @Test
    public void testSendConfirmation_BookingNotFound() {
        // Arrange
        when(bookingManager.retrieveBooking("B999")).thenReturn(null);

        // Act
        boolean result = bookingController.requestConfirmation("B999", "Email");

        // Assert
        assertFalse(result);
    }

    // UC10: View Customer History
//    @Test
//    public void testViewCustomerHistory_HappyPath() {
//        // Arrange
//        // Create a mock booking system that returns a list with bookings for customer 1
//        Customer customer = new Customer(1, "Malinda Gamage", "973727398V", "pkgmalinda@gmail.com");
//        Table table = new Table(1, 4, "Window");
//        Booking booking = new Booking("B1", table, customer, LocalDateTime.now(), 4, "");
//        List<Booking> bookings = new ArrayList<>();
//        bookings.add(booking);
//
//        // Mock the behavior of the booking system to return the list of bookings
//        when(bookingSystem.getBookings()).thenReturn(bookings);
//
//        // Act
//        Customer result = bookingController.viewCustomerHistory(1);
//
//        // Assert
//        assertNotNull(result);  // Ensure the customer is not null
//        assertEquals("Malinda Gamage", result.getName());  // Check if the name matches
//        assertEquals(1, result.getBookingHistory().size());  // Check if there is one booking in history
//    }

    @Test
    public void testViewCustomerHistory_CustomerNotFound() {
        // Act
        Customer result = bookingController.viewCustomerHistory(999);

        // Assert
        assertNull(result);
    }

    @Test
    public void testViewCustomerHistory_EmptyBookings() {
        // Arrange
        when(bookingSystem.getBookings()).thenReturn(new ArrayList<>());

        // Act
        Customer result = bookingController.viewCustomerHistory(1);

        // Assert
        assertNull(result);
    }

    // UC11: Manage System Settings
    @Test
    public void testManageSystemSettings_HappyPath() {
        // Arrange
        Manager manager = new Manager(2, "manager1", "password", "Manager", 2, "Malinda Gamage", "jane@example.com", 1,
                bookingController, reportController);

        // Act
        manager.manageSystemSettings();

        // Assert
        // Since manageSystemSettings just prints a message, we can only verify it doesn't throw an exception
        assertTrue(true);
    }

    // UC1: Check Table Availability - Main Success Scenario
    @Test
    public void testCheckTableAvailability_HappyPath() { // Already exists, but ensuring it matches UC1
        // Arrange
        Table table1 = new Table(1, 4, "Window");
        Table table2 = new Table(2, 6, "Center");
        Table[] availableTables = {table1, table2};
        when(tableManager.findAvailableTables("2025-04-04", "18:00", 4)).thenReturn(availableTables);

        // Act
        Table[] result = bookingController.checkAvailability("2025-04-04", "18:00", 4);

        // Assert
        assertEquals(2, result.length);
        assertEquals("T1", result[0].getTableId());
        assertEquals("T2", result[1].getTableId());
    }

    // UC1 Alternative Flow: No Tables Available
    @Test
    public void testCheckTableAvailability_NoTablesAvailable() { // Already exists, renamed for clarity
        // Arrange
        when(tableManager.findAvailableTables("2025-04-04", "18:00", 4)).thenReturn(new Table[0]);

        // Act
        Table[] result = bookingController.checkAvailability("2025-04-04", "18:00", 4);

        // Assert
        assertEquals(0, result.length);
    }

    // UC1 Edge Case: Invalid Date Format (Already exists)
    @Test
    public void testCheckTableAvailability_InvalidDateFormat() {
        // Act & Assert
        assertThrows(DateTimeParseException.class, () -> {
            bookingController.checkAvailability("2025-13-40", "18:00", 4);
        });
    }

    // UC2: Create Booking - Main Success Scenario
    @Test
    public void testCreateBooking_HappyPath() {
        // Arrange
        BookingController bookingController = new BookingController(); // Real instance
        Customer customer = new Customer(1, "Malinda Gamage", "973727398V", "pkgmalinda@gmail.com");
        BookingDetails bookingDetails = new BookingDetails(LocalDateTime.of(2025, 4, 4, 18, 0), 4, "Window seat");
        String tableId = "T1";

        // Act
        Booking result = bookingController.createBooking(tableId, customer, bookingDetails);

        // Assert
        assertNotNull(result); // Step 4: Booking recorded
        assertEquals(tableId, result.getTable().getTableId()); // Step 1: Table selected
        assertEquals(customer, result.getCustomer()); // Step 2: Customer details entered
        assertEquals(4, result.getGuests()); // Step 3: Number of guests confirmed
        assertEquals("Window seat", result.getSpecialRequirements()); // Step 3: Special requirements confirmed
        assertNotNull(result.getBookingRef()); // Step 4: Unique reference assigned
        // Step 5: Table status update isn’t directly testable here (handled by BookingManager)
        // Step 6: Confirmation details generated (assumed in Booking creation)
        assertEquals(1, BookingSystem.getInstance().getBookings().size()); // Booking added to system
        assertEquals(result, BookingSystem.getInstance().getBookings().get(0));
    }

    // UC2 Alternative Flow: Duplicate Booking Detected
    @Test
    public void testCreateBooking_DuplicateBooking() {
        // Arrange
        BookingController bookingController = new BookingController();
        Customer customer = new Customer(1, "Malinda Gamage", "973727398V", "pkgmalinda@gmail.com");
        BookingDetails bookingDetails = new BookingDetails(LocalDateTime.of(2025, 4, 4, 18, 0), 4, "Window seat");
        String tableId = "T1";

        // Add existing booking
        Booking existingBooking = new Booking("B1", new Table(1, 4, "Window"), customer,
                LocalDateTime.of(2025, 4, 4, 18, 0), 4, "Window seat");
        BookingSystem.getInstance().getBookings().add(existingBooking);

        // Act
        Booking result = bookingController.createBooking(tableId, customer, bookingDetails);

        // Assert
        assertNull(result); // Assuming duplicate detection returns null
        assertEquals(1, BookingSystem.getInstance().getBookings().size()); // No new booking added
    }

    // UC3: Modify Booking - Main Success Scenario
    @Test
    public void testModifyBooking_HappyPath() {
        // Arrange
        BookingController bookingController = new BookingController(); // Real instance
        Customer customer = new Customer(1, "Malinda Gamage", "973727398V", "pkgmalinda@gmail.com");
        Table table = new Table(1, 4, "Window");
        Booking booking = new Booking("B1", table, customer, LocalDateTime.of(2025, 4, 4, 18, 0), 4, "Window seat");
        BookingSystem.getInstance().getBookings().add(booking); // Add existing booking
        BookingDetails updatedDetails = new BookingDetails(LocalDateTime.of(2025, 4, 4, 19, 0), 5, "Corner seat");

        // Act
        boolean result = bookingController.updateBooking("B1", updatedDetails);

        // Assert
        assertTrue(result); // Step 5: Booking updated successfully
        Booking updatedBooking = BookingSystem.getInstance().getBookings().get(0);
        assertEquals(LocalDateTime.of(2025, 4, 4, 19, 0), updatedBooking.getDateTime()); // Step 3 & 5: Date/time modified
        assertEquals(5, updatedBooking.getGuests()); // Step 3 & 5: Guests modified
        assertEquals("Corner seat", updatedBooking.getSpecialRequirements()); // Step 3 & 5: Special requirements modified
    }

    // UC3 Alternative Flow: Requested Changes Cannot Be Accommodated
    @Test
    public void testModifyBooking_ChangesNotAccommodated() {
        // Arrange
        // Use mocking to simulate BookingManager rejecting the change
        BookingManager mockedBookingManager = mock(BookingManager.class);
        BookingController bookingController = new BookingController() {
            // Override to use mocked BookingManager
            {
                this.bookingManager = mockedBookingManager;
            }
        };
        Customer customer = new Customer(1, "Malinda Gamage", "973727398V", "pkgmalinda@gmail.com");
        Table table = new Table(1, 4, "Window");
        Booking booking = new Booking("B1", table, customer, LocalDateTime.of(2025, 4, 4, 18, 0), 4, "Window seat");
        BookingSystem.getInstance().getBookings().add(booking);
        BookingDetails updatedDetails = new BookingDetails(LocalDateTime.of(2025, 4, 4, 19, 0), 5, "Corner seat");

        // Simulate no table availability for the new time
        when(mockedBookingManager.retrieveBooking("B1")).thenReturn(booking);
        when(mockedBookingManager.modifyBooking("B1", updatedDetails)).thenReturn(false);

        // Act
        boolean result = bookingController.updateBooking("B1", updatedDetails);

        // Assert
        assertFalse(result); // Step 1: System indicates constraints
        Booking unchangedBooking = BookingSystem.getInstance().getBookings().get(0);
        assertEquals(LocalDateTime.of(2025, 4, 4, 18, 0), unchangedBooking.getDateTime()); // Step 4: Original booking maintained
        assertEquals(4, unchangedBooking.getGuests()); // Step 4: Original booking maintained
        assertEquals("Window seat", unchangedBooking.getSpecialRequirements()); // Step 4: Original booking maintained
    }

    // UC4: Cancel Booking - Main Success Scenario
    @Test
    public void testCancelBooking_HappyPath() {
        // Arrange
        BookingController bookingController = new BookingController(); // Real instance
        Customer customer = new Customer(1, "Malinda Gamage", "973727398V", "pkgmalinda@gmail.com");
        Table table = new Table(1, 4, "Window");
        Booking booking = new Booking("B1", table, customer, LocalDateTime.of(2025, 4, 4, 18, 0), 4, "Window seat");
        BookingSystem.getInstance().getBookings().add(booking);

        // Act
        boolean result = bookingController.deleteBooking("B1"); // Steps 3-5: Staff cancels, system confirms

        // Assert
        assertTrue(result); // Step 6: Cancellation successful
        Booking updatedBooking = BookingSystem.getInstance().getBookings().get(0);
        assertEquals("Canceled", updatedBooking.getState().getStateName()); // Step 6: Booking state updated
        // Note: Table status update to "Available" (Step 6) and history log (Step 7) aren’t directly testable here
        // unless BookingManager.cancelBooking() explicitly updates table status
    }

    // UC4 Alternative Flow: Staff Decides Not to Cancel
    @Test
    public void testCancelBooking_StaffDecidesNotToCancel() {
        // Arrange
        // Use mocking to simulate staff not confirming cancellation
        BookingManager mockedBookingManager = mock(BookingManager.class);
        BookingController bookingController = new BookingController() {
            // Override to use mocked BookingManager
            {
                this.bookingManager = mockedBookingManager;
            }
        };
        Customer customer = new Customer(1, "Malinda Gamage", "973727398V", "pkgmalinda@gmail.com");
        Table table = new Table(1, 4, "Window");
        Booking booking = new Booking("B1", table, customer, LocalDateTime.of(2025, 4, 4, 18, 0), 4, "Window seat");
        BookingSystem.getInstance().getBookings().add(booking);

        // Simulate staff selecting "No" by having cancelBooking return false
        when(mockedBookingManager.retrieveBooking("B1")).thenReturn(booking);
        when(mockedBookingManager.cancelBooking("B1")).thenReturn(false);

        // Act
        boolean result = bookingController.deleteBooking("B1");

        // Assert
        assertFalse(result); // Step 1: Staff selects "No"
        Booking unchangedBooking = BookingSystem.getInstance().getBookings().get(0);
        assertNotEquals("Canceled", unchangedBooking.getState().getStateName()); // Step 2: Booking unchanged
    }

    // UC5: Record Walk-in - Main Success Scenario
    @Test
    public void testRecordWalkin_HappyPath() {
        // Arrange
        BookingController bookingController = new BookingController(); // Real instance
        Customer customer = new Customer(2, "Malinda Gamage", "973727398V", "pkgmalinda@gmail.com");
        Table table = new Table(1, 4, "Window");
        Table[] availableTables = {table};

        // Mock getCurrentAvailability to simulate available tables
        BookingController spiedController = spy(bookingController);
        doReturn(availableTables).when(spiedController).getCurrentAvailability(2);

        // Act
        WalkinRecord result = spiedController.recordWalkin("T1", customer, 2);

        // Assert
        assertNotNull(result); // Step 5: Walk-in recorded
        assertEquals("T1", result.getTable().getTableId()); // Step 3: Table selected
        assertEquals("Occupied", result.getTable().getStatus()); // Step 5: Table status updated
        assertEquals("Malinda Gamage", result.getCustomerName()); // Step 4: Customer info recorded
        // Step 6: Walk-in recorded in history (not directly testable unless stored in a list)
    }

    // UC5 Alternative Flow: No Tables Currently Available
    @Test
    public void testRecordWalkin_NoTablesAvailable() {
        // Arrange
        BookingController bookingController = new BookingController(); // Real instance
        Customer customer = new Customer(2, "Malinda Gamage", "973727398V", "pkgmalinda@gmail.com");

        // Mock getCurrentAvailability to simulate no available tables
        BookingController spiedController = spy(bookingController);
        doReturn(new Table[0]).when(spiedController).getCurrentAvailability(2);

        // Act
        WalkinRecord result = spiedController.recordWalkin("T1", customer, 2);

        // Assert
        assertNull(result); // Step 1: System indicates no availability
        // Steps 2-4 (wait time estimate and waiting list) are staff actions not directly testable here
    }

    // UC6: Generate Reports - Main Success Scenario
    @Test
    public void testGenerateReports_HappyPath() {
        // Arrange
        Report report = new Report("R1", "Booking", "All");
        List<String> reportData = new ArrayList<>();
        reportData.add("Booking B1: Kumar Sangakkara, 4 guests"); // Simulated report content
        report.setData(reportData); // Assuming Report has a setData method
        when(reportManager.createReport("Booking", "All")).thenReturn(report);

        // Act
        Report result = reportController.generateReport("Booking", "All");

        // Assert
        assertNotNull(result); // Step 4: Report generated
        assertEquals("R1", result.getReportId()); // Step 4: Unique report ID
        assertEquals("Booking", result.getReportType()); // Step 3: Correct report type
        assertEquals("All", result.getParameters()); // Step 3: Correct parameters
        assertFalse(result.getData().isEmpty()); // Step 5: Report contains data
        assertEquals(1, result.getData().size()); // Step 5: Data present
    }

    // UC6 Alternative Flow: Insufficient Data for Meaningful Report
    @Test
    public void testGenerateReports_InsufficientData() {
        // Arrange
        // Simulate no data by clearing BookingSystem bookings
        BookingSystem.getInstance().getBookings().clear();
        try (MockedStatic<BookingSystem> mockedStatic = mockStatic(BookingSystem.class)) {
            mockedStatic.when(BookingSystem::getInstance).thenReturn(bookingSystem);
            when(bookingSystem.getBookings()).thenReturn(new ArrayList<>()); // Empty bookings

            Report report = new Report("R2", "Booking", "All");
            report.setData(Collections.emptyList()); // Empty report data
            when(reportManager.createReport("Booking", "All")).thenReturn(report);

            // Act
            Report result = reportController.generateReport("Booking", "All");

            // Assert
            assertNotNull(result); // Step 1: Report generated despite no data
            assertEquals("R2", result.getReportId());
            assertEquals("Booking", result.getReportType());
            assertEquals("All", result.getParameters());
            assertTrue(result.getData().isEmpty()); // Step 1: Indicates data limitations
        }
    }

    // UC7: Update Table Status - Main Success Scenario
    @Test
    public void testUpdateTableStatus_HappyPath() {
        // Arrange
        when(tableManager.updateTableStatus(eq("T1"), eq("Needs Cleaning"), any(LocalDateTime.class))).thenReturn(true);

        // Act
        boolean result = bookingController.updateTableStatus("T1", "Needs Cleaning");

        // Assert
        assertTrue(result); // Step 6: Table status updated successfully
        verify(tableManager, times(1)).updateTableStatus(eq("T1"), eq("Needs Cleaning"), any(LocalDateTime.class));
    }

    // UC7 Alternative Flow: Staff Updates Multiple Tables
    @Test
    public void testUpdateMultipleTableStatuses() {
        // Arrange
        List<String> tableIds = Arrays.asList("T1", "T2");
        when(tableManager.updateTableStatuses(eq(tableIds), eq("Available"), any(LocalDateTime.class))).thenReturn(true);

        // Act
        boolean result = bookingController.updateTableStatuses(tableIds, "Available");

        // Assert
        assertTrue(result); // Step 2: All tables updated successfully
        verify(tableManager, times(1)).updateTableStatuses(eq(tableIds), eq("Available"), any(LocalDateTime.class));
    }

    // Additional Test: Partial Failure in Bulk Update
    @Test
    public void testUpdateMultipleTableStatuses_PartialFailure() {
        // Arrange
        List<String> tableIds = Arrays.asList("T1", "T3"); // T3 doesn’t exist or fails
        when(tableManager.updateTableStatuses(eq(tableIds), eq("Available"), any(LocalDateTime.class))).thenReturn(false);

        // Act
        boolean result = bookingController.updateTableStatuses(tableIds, "Available");

        // Assert
        assertFalse(result); // Step 2: Bulk update fails if any table update fails
        verify(tableManager, times(1)).updateTableStatuses(eq(tableIds), eq("Available"), any(LocalDateTime.class));
    }

    // UC8: Manage Waiting List - Main Success Scenario
    @Test
    public void testManageWaitingList_HappyPath() {
        // Arrange
        int entryId = 1;
        String customerName = "Malinda Gamage";
        int guests = 4;
        String contactNumber = "9876543210";
        int estimatedWaitTime = 15;
        WaitingListEntry entry = new WaitingListEntry(entryId, customerName, contactNumber, guests, LocalDateTime.now());
        entry.setEstimatedWaitTime(estimatedWaitTime);
        when(waitingListManager.addToWaitingList(customerName, guests, contactNumber, estimatedWaitTime)).thenReturn(true);
        when(waitingListManager.getNextCustomer()).thenReturn(entry);
        when(waitingListManager.removeFromWaitingList(entryId)).thenReturn(entry);

        // Act
        boolean added = bookingController.addToWaitingList(customerName, guests, contactNumber, estimatedWaitTime); // Steps 2-4
        WaitingListEntry nextCustomer = bookingController.getNextWaitingCustomer(); // Step 5
        Booking result = bookingController.convertWaitingToBooking(entryId, "T1"); // Steps 6-7

        // Assert
        assertTrue(added); // Step 4: Added to waiting list
        assertNotNull(nextCustomer); // Step 5: Displayed in priority order
        assertEquals(customerName, nextCustomer.getCustomerName());
        assertNotNull(result); // Step 7: Converted to booking
        assertEquals("B1", result.getBookingRef()); // Matches entryId
        verify(waitingListManager, times(1)).addToWaitingList(customerName, guests, contactNumber, estimatedWaitTime);
        verify(waitingListManager, times(1)).getNextCustomer();
        verify(waitingListManager, times(1)).removeFromWaitingList(entryId);
    }

    // UC8 Alternative Flow: Customer Cannot Be Reached
    @Test
    public void testManageWaitingList_CustomerCannotBeReached() {
        // Arrange
        WaitingListEntry entry1 = new WaitingListEntry(1, "Malinda Gamage", "9876543210", 4, LocalDateTime.now());
        WaitingListEntry entry2 = new WaitingListEntry(2, "Kumar Sangakkara", "5551234567", 4, LocalDateTime.now().plusSeconds(1));

        when(waitingListManager.addToWaitingList(eq("Malinda Gamage"), eq(4), eq("9876543210"), eq(15))).thenReturn(true);
        when(waitingListManager.addToWaitingList(eq("Kumar Sangakkara"), eq(4), eq("5551234567"), eq(15))).thenReturn(true);
        when(waitingListManager.getNextCustomer()).thenReturn(entry1, entry2);
        when(waitingListManager.getWaitingList()).thenReturn(new ArrayList<>(Arrays.asList(entry1, entry2)));

        doAnswer(invocation -> {
            entry1.updateStatus("Attempted Contact");
            return null;
        }).when(waitingListManager).markAttemptedContact(1);

        // Act
        bookingController.addToWaitingList("Malinda Gamage", 4, "9876543210", 15);
        bookingController.addToWaitingList("Kumar Sangakkara", 4, "5551234567", 15);
        WaitingListEntry firstCustomer = bookingController.getNextWaitingCustomer();
        bookingController.markAttemptedContact(1);
        WaitingListEntry nextCustomer = bookingController.getNextWaitingCustomer();

        // Assert
        assertEquals("Malinda Gamage", firstCustomer.getCustomerName());
        assertEquals("Attempted Contact", firstCustomer.getStatus(), "Status should be updated to 'Attempted Contact'");
        assertEquals("Kumar Sangakkara", nextCustomer.getCustomerName());
        assertEquals("Waiting", nextCustomer.getStatus());
        assertTrue(waitingListManager.getWaitingList().contains(entry1), "Malinda should still be on the list");
        verify(waitingListManager, times(1)).markAttemptedContact(1);
        verify(waitingListManager, times(2)).getNextCustomer();
    }

    // UC9: Send Confirmation - Main Success Scenario
    @Test
    public void testSendConfirmation_HappyPath() {
        // Arrange
        Booking realBooking = new Booking("B1", new Table(1, 4, "Window"),
                new Customer(1, "Kumar Sangakkara", "1234567890", "pkgmalinda@gmail.com"),
                LocalDateTime.now(), 4, "");
        Booking booking = spy(realBooking); // Spy to allow stubbing on real object
        when(bookingManager.retrieveBooking("B1")).thenReturn(booking);
        doAnswer(invocation -> {
            booking.setConfirmationStatus("Sent via Email"); // Simulate successful send
            return true;
        }).when(booking).sendConfirmation("Email");

        // Act
        boolean result = bookingController.requestConfirmation("B1", "Email"); // Steps 1-4

        // Assert
        assertTrue(result); // Step 4: Confirmation sent successfully
        assertEquals("Sent via Email", booking.getConfirmationStatus()); // Step 5: Recorded in booking
        verify(bookingManager, times(1)).retrieveBooking("B1");
        verify(booking, times(1)).sendConfirmation("Email");
    }

    // UC9 Alternative Flow: Delivery Fails
    @Test
    public void testSendConfirmation_DeliveryFails() {
        // Arrange
        Booking realBooking = new Booking("B1", new Table(1, 4, "Window"),
                new Customer(1, "Kumar Sangakkara", "1234567890", "pkgmalinda@gmail.com"),
                LocalDateTime.now(), 4, "");
        Booking booking = spy(realBooking); // Spy to allow stubbing on real object
        when(bookingManager.retrieveBooking("B1")).thenReturn(booking);

        // Simulate email failure
        doAnswer(invocation -> {
            booking.setConfirmationStatus("Failed: Email"); // Step 1: Indicate failure
            return false;
        }).when(booking).sendConfirmation("Email");

        // Simulate SMS attempt failure
        doAnswer(invocation -> {
            booking.setConfirmationStatus("Failed: Email, SMS"); // Step 2: Try alternative, still fails
            return false;
        }).when(booking).sendConfirmation("SMS");

        // Act
        boolean emailResult = bookingController.requestConfirmation("B1", "Email"); // Step 1
        boolean smsResult = bookingController.requestConfirmation("B1", "SMS"); // Step 2

        // Assert
        assertFalse(emailResult); // Step 1: Email delivery failed
        assertFalse(smsResult); // Step 2: SMS delivery failed
        assertEquals("Failed: Email, SMS", booking.getConfirmationStatus()); // Step 3: Noted in system
        verify(bookingManager, times(2)).retrieveBooking("B1"); // Called twice for both attempts
        verify(booking, times(1)).sendConfirmation("Email");
        verify(booking, times(1)).sendConfirmation("SMS");
    }

    // UC10 Alternative Flow: Multiple Customers with Similar Details
    @Test
    public void testViewCustomerHistory_MultipleMatches() {
        // Arrange
        BookingSystem.getInstance().getBookings().clear(); // Reset singleton state
        Customer customer1 = new Customer(1, "Kumar Sangakkara", "1234567890", "kumar1@example.com");
        Customer customer2 = new Customer(2, "Kumar Sangakkara", "1234567890", "kumar2@example.com");
        Booking booking1 = new Booking("B1", new Table(1, 4, "Window"), customer1, LocalDateTime.now().minusDays(1), 4, "");
        Booking booking2 = new Booking("B2", new Table(2, 4, "Corner"), customer2, LocalDateTime.now().minusDays(2), 4, "");
        List<Booking> bookings = Arrays.asList(booking1, booking2);
        BookingSystem.getInstance().getBookings().addAll(bookings); // Populate singleton directly

        // Act
        Customer result1 = bookingController.viewCustomerHistory(1);
        Customer result2 = bookingController.viewCustomerHistory(2);

        // Assert
        assertNotNull(result1, "Customer 1 should be found");
        assertEquals(1, result1.getCustomerId());
        assertEquals(1, result1.getHistory().size());
        assertTrue(result1.getHistory().contains(booking1), "Customer 1 should have booking B1");

        assertNotNull(result2, "Customer 2 should be found");
        assertEquals(2, result2.getCustomerId());
        assertEquals(1, result2.getHistory().size());
        assertTrue(result2.getHistory().contains(booking2), "Customer 2 should have booking B2");

        // No need to verify bookingSystem.getBookings() since we're using the singleton
    }

    // UC10 Alternative Flow: No Customer Record Found
    @Test
    public void testViewCustomerHistory_NoRecordFound() {
        // Arrange
        BookingSystem.getInstance().getBookings().clear(); // Reset singleton state
        Customer customer = new Customer(1, "John Doe", "1234567890", "john@example.com");
        Booking booking = new Booking("B1", new Table(1, 4, "Window"), customer, LocalDateTime.now(), 4, "");
        BookingSystem.getInstance().getBookings().add(booking); // Add one booking to singleton

        // Act
        Customer result = bookingController.viewCustomerHistory(999);

        // Assert
        assertNull(result, "No customer should be found for ID 999");
    }
}