package bakendi;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BookingManager {
    private static final List<String> bookings = new ArrayList<>();

    public static void addBooking(Tour tour, int people, LocalDate date, boolean hotelPickup) {
        if (date == null) {
            System.err.println("No date selected for booking.");
            return;
        }

        String bookingDetails = "Tour: " + tour.getName() +
                ", People: " + people +
                ", Date: " + date +
                ", Hotel Pickup: " + (hotelPickup ? "Yes" : "No");

        bookings.add(bookingDetails);
        System.out.println("Booking stored: " + bookingDetails);
    }

    public static List<String> getBookings() {
        return bookings;
    }

    public static void clearBookings() {
        bookings.clear();
    }
}
