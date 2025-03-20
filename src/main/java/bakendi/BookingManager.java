package bakendi;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BookingManager {

    public static boolean addBooking(Tour tour, int amountPeople, LocalDate date, boolean hotelPickup) {
        int userId = UserRepository.getCurrentUserId();

        if (userId == -1) {
            System.err.println("No user logged in.");
            return false;
        }

        String sql = "INSERT INTO Bookings (user_id, tour_name, amount_people, date, hotel_pickup) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnector.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, userId);
            preparedStatement.setString(2, tour.getName());
            preparedStatement.setInt(3, amountPeople);
            preparedStatement.setString(4, date.toString());
            preparedStatement.setBoolean(5, hotelPickup);
            preparedStatement.executeUpdate();
            System.out.println("Booking successful for user: " + userId);
            return true;
        } catch (SQLException e) {
            System.err.println("Booking failed: " + e.getMessage());
            return false;
        }
    }

    public static List<Booking> getBookingsForUser() {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT id, tour_name, amount_people, date, hotel_pickup FROM Bookings WHERE user_id = ?";

        try (Connection connection = DatabaseConnector.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, UserRepository.getCurrentUserId());
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String tour = resultSet.getString("tour_name");
                int people = resultSet.getInt("amount_people");
                String date = resultSet.getString("date");
                boolean pickup = resultSet.getBoolean("hotel_pickup");

                Booking bookingInfo = new Booking(id, tour, people, date, pickup);
                bookings.add(bookingInfo);
                System.out.println("Loaded Booking: " + bookingInfo);
            }
            System.out.println("Loaded " + bookings.size() + " bookings from database.");
        } catch (SQLException e) {
            System.err.println("Error fetching bookings: " + e.getMessage());
        }

        return bookings;
    }

    public static void removeBooking(int bookingId) {
        String sql = "DELETE FROM Bookings WHERE id = ?";

        try (Connection connection = DatabaseConnector.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, bookingId);
            preparedStatement.executeUpdate();
            System.out.println("Booking ID " + bookingId + " canceled.");
        } catch (SQLException e) {
            System.err.println("Error canceling booking: " + e.getMessage());
        }
    }

}
