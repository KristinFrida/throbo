package bakendi;

import java.util.ArrayList;
import java.util.List;

public class BookingManager {
    private static final List<Tour> bookings = new ArrayList<>();

    public static void addBooking(Tour tour) {
        bookings.add(tour);
    }

    public static List<Tour> getBookings() {
        return bookings;
    }

    public static void clearBookings() {
        bookings.clear();
    }
}
