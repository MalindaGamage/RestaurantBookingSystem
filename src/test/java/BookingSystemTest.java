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
        List<Booking> bookings = new ArrayList<>();
        Customer customer = new Customer(1, "John Doe", "1234567890", "john@example.com");
        Table table = new Table(1, 4, "Window");
        Booking booking = new Booking("B1", table, customer, LocalDateTime.now(), 4, "");
        bookings.add(booking);
        when(bookingSystem.getBookings()).thenReturn(bookings);
    }

    // UC1: Check Table Availability
    @Test
    public void testCheckTableAvailability_HappyPath() {
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

    @Test
    public void testCheckTableAvailability_NoTablesAvailable() {
        // Arrange
        when(tableManager.findAvailableTables("2025-04-04", "18:00", 4)).thenReturn(new Table[0]);

        // Act
        Table[] result = bookingController.checkAvailability("2025-04-04", "18:00", 4);

        // Assert
        assertEquals(0, result.length);
    }

    @Test
    public void testCheckTableAvailability_InvalidDateFormat() {
        // Act & Assert
        assertThrows(DateTimeParseException.class, () -> {
            bookingController.checkAvailability("2025-13-40", "18:00", 4);
        });
    }

    // UC2: Create Booking
    @Test
    public void testCreateBooking_HappyPath() {
        // Arrange
        Customer customer = new Customer(1, "John Doe", "1234567890", "john@example.com");
        Table table = new Table(1, 4, "Window");
        BookingDetails bookingDetails = new BookingDetails(LocalDateTime.of(2025, 4, 4, 18, 0), 4, "Window seat");
        Booking booking = new Booking("B1", table, customer, bookingDetails.getDateTime(), 4, "Window seat");
        when(bookingManager.registerBooking("T1", customer, bookingDetails)).thenReturn(booking);

        // Mock BookingSystem.getInstance() to return the mocked bookingSystem
        try (MockedStatic<BookingSystem> mockedStatic = mockStatic(BookingSystem.class)) {
            mockedStatic.when(BookingSystem::getInstance).thenReturn(bookingSystem);
            List<Booking> bookingsList = new ArrayList<>();
            when(bookingSystem.getBookings()).thenReturn(bookingsList);

            // Act
            Booking result = bookingController.createBooking("T1", customer, bookingDetails);

            // Assert
            assertNotNull(result);
            assertEquals("B1", result.getBookingRef());
            assertEquals(4, result.getGuests());
            verify(bookingSystem, times(1)).getBookings();
            assertEquals(1, bookingsList.size());
            assertEquals(booking, bookingsList.get(0));
        }
    }

    @Test
    public void testCreateBooking_TableNotAvailable() {
        // Arrange
        Customer customer = new Customer(1, "John Doe", "1234567890", "john@example.com");
        BookingDetails bookingDetails = new BookingDetails(LocalDateTime.of(2025, 4, 4, 18, 0), 4, "Window seat");
        when(bookingManager.registerBooking("T1", customer, bookingDetails)).thenReturn(null);

        // Act
        Booking result = bookingController.createBooking("T1", customer, bookingDetails);

        // Assert
        assertNull(result);
    }

    // UC3: Modify Booking
//    @Test
//    public void testModifyBooking_HappyPath() {
//        // Arrange
//        Booking booking = new Booking("B1", new Table(1, 4, "Window"),
//                new Customer(1, "John Doe", "1234567890", "john@example.com"),
//                LocalDateTime.of(2025, 4, 4, 18, 0), 4, "");
//        when(bookingManager.retrieveBooking("B1")).thenReturn(booking);
//        when(bookingManager.modifyBooking("B1", any(BookingDetails.class))).thenReturn(true);
//
//        BookingDetails updatedDetails = new BookingDetails(LocalDateTime.of(2025, 4, 5, 19, 0), 5, "");
//
//        // Act
//        boolean result = bookingController.updateBooking("B1", updatedDetails);
//
//        // Assert
//        assertTrue(result);
//        assertEquals(LocalDateTime.of(2025, 4, 5, 19, 0), booking.getDateTime());
//        assertEquals(5, booking.getGuests());
//    }

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
                new Customer(1, "John Doe", "1234567890", "john@example.com"),
                LocalDateTime.of(2025, 4, 4, 18, 0), 4, "");
        booking.setState(new CanceledState());
        when(bookingManager.retrieveBooking("B1")).thenReturn(booking);

        BookingDetails updatedDetails = new BookingDetails(LocalDateTime.of(2025, 4, 5, 19, 0), 5, "");

        // Act
        boolean result = bookingController.updateBooking("B1", updatedDetails);

        // Assert
        assertFalse(result);
    }

    // UC4: Cancel Booking
    @Test
    public void testCancelBooking_HappyPath() {
        // Arrange
        BookingManager bookingManager = mock(BookingManager.class);
        Booking booking = new Booking("B1", new Table(1, 4, "Window"),
                new Customer(1, "John Doe", "1234567890", "john@example.com"),
                LocalDateTime.of(2025, 4, 4, 18, 0), 4, "");
        BookingSystem.getInstance().getBookings().add(booking);
        when(bookingManager.retrieveBooking("B1")).thenReturn(booking);


        // Act
        boolean result = bookingController.deleteBooking("B1");

        // Assert
        assertTrue(result);
        assertEquals("Canceled", booking.getState().getStateName());  // Verify that the state is set to Canceled
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
                new Customer(1, "John Doe", "1234567890", "john@example.com"),
                LocalDateTime.of(2025, 4, 4, 18, 0), 4, "");
        booking.setState(new CanceledState());
        when(bookingManager.retrieveBooking("B1")).thenReturn(booking);
        when(bookingManager.cancelBooking("B1")).thenReturn(false);

        // Act
        boolean result = bookingController.deleteBooking("B1");

        // Assert
        assertFalse(result);
    }

    // UC5: Record Walk-in
    @Test
    public void testRecordWalkin_HappyPath() {
        // Arrange
        Customer customer = new Customer(2, "Jane Doe", "", "");
        Table table = new Table(1, 4, "Window");
        Table[] availableTables = {table};
        when(bookingController.getCurrentAvailability(2)).thenReturn(availableTables);
        WalkinRecord walkin = new WalkinRecord("W1", table, "Jane Doe", 2, LocalDateTime.now());
        when(bookingManager.createWalkinRecord("T1", customer)).thenReturn(walkin);

        // Act
        WalkinRecord result = bookingController.recordWalkin("T1", customer);

        // Assert
        assertNotNull(result);
        assertEquals("W1", result.getRecordId());
        assertEquals("Occupied", table.getStatus());
    }

    @Test
    public void testRecordWalkin_NoTablesAvailable() {
        // Arrange
        Customer customer = new Customer(2, "Jane Doe", "", "");
        when(bookingController.getCurrentAvailability(2)).thenReturn(new Table[0]);

        // Act
        WalkinRecord result = bookingController.recordWalkin("T1", customer);

        // Assert
        assertNull(result);
    }

    // UC6: Generate Reports
    @Test
    public void testGenerateReports_HappyPath() {
        // Arrange
        Report report = new Report("R1", "Booking", "All");
        when(reportManager.createReport("Booking", "All")).thenReturn(report);

        // Act
        Report result = reportController.generateReport("Booking", "All");

        // Assert
        assertNotNull(result);
        assertEquals("R1", result.getReportId());
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

    // UC7: Update Table Status
    @Test
    public void testUpdateTableStatus_HappyPath() {
        // Arrange
        when(tableManager.updateTableStatus(eq("T1"), eq("Needs Cleaning"), any(LocalDateTime.class))).thenReturn(true);

        // Act
        boolean result = bookingController.updateTableStatus("T1", "Needs Cleaning");

        // Assert
        assertTrue(result);
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

    // UC8: Manage Waiting List
    @Test
    public void testManageWaitingList_HappyPath() {
        // Arrange
        Customer customer = new Customer(3, "Alice", "9876543210", "");
        WaitingListEntry entry = new WaitingListEntry(1, "Alice", "9876543210", 3, LocalDateTime.now());
        when(waitingListManager.getNextInLine()).thenReturn(entry);

        // Act
        bookingController.manageWaitingList();

        // Assert
        verify(waitingListManager, times(1)).manageWaitingList();
    }

    @Test
    public void testManageWaitingList_EmptyList() {
        // Arrange
        when(waitingListManager.getNextInLine()).thenReturn(null);

        // Act
        bookingController.manageWaitingList();

        // Assert
        verify(waitingListManager, times(1)).manageWaitingList();
    }

    // UC9: Send Confirmation
    @Test
    public void testSendConfirmation_HappyPath() {
        // Arrange
        Booking booking = mock(Booking.class);
        when(bookingManager.retrieveBooking("B1")).thenReturn(booking);
        when(booking.sendConfirmation("Email")).thenReturn(true);

        // Act
        boolean result = bookingController.requestConfirmation("B1", "Email");

        // Assert
        assertTrue(result);
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
    @Test
    public void testViewCustomerHistory_HappyPath() {
        // Arrange
        // Create a mock booking system that returns a list with bookings for customer 1
        Customer customer = new Customer(1, "John Doe", "1234567890", "john@example.com");
        Table table = new Table(1, 4, "Window");
        Booking booking = new Booking("B1", table, customer, LocalDateTime.now(), 4, "");
        List<Booking> bookings = new ArrayList<>();
        bookings.add(booking);

        // Mock the behavior of the booking system to return the list of bookings
        when(bookingSystem.getBookings()).thenReturn(bookings);

        // Act
        Customer result = bookingController.viewCustomerHistory(1);

        // Assert
        assertNotNull(result);  // Ensure the customer is not null
        assertEquals("John Doe", result.getName());  // Check if the name matches
        assertEquals(1, result.getBookingHistory().size());  // Check if there is one booking in history
    }

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
        Manager manager = new Manager(2, "manager1", "password", "Manager", 2, "Jane Doe", "jane@example.com", 1,
                bookingController, reportController);

        // Act
        manager.manageSystemSettings();

        // Assert
        // Since manageSystemSettings just prints a message, we can only verify it doesn't throw an exception
        assertTrue(true);
    }
}