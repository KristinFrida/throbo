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

    public static List<String> getBookings() {
        int userId = UserRepository.getCurrentUserId();

        if (userId == -1) {
            System.out.println("No user logged in.");
            return new ArrayList<>();
        }

        List<String> bookings = new ArrayList<>();
        String sql = "SELECT tour_name, amount_people, date, hotel_pickup FROM Bookings WHERE user_id = ?";

        try (Connection connection = DatabaseConnector.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String tourName = resultSet.getString("tour_name");
                int people = resultSet.getInt("amount_people");
                String date = resultSet.getString("date");
                boolean hotelPickup = resultSet.getBoolean("hotel_pickup");

                String bookingDetails = "Tour: " + tourName +
                        ", People: " + people +
                        ", Date: " + date +
                        ", Hotel Pickup: " + (hotelPickup ? "Yes" : "No");
                bookings.add(bookingDetails);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching bookings: " + e.getMessage());
        }

        return bookings;
    }
}
