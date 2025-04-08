package backend;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRepository {

    private static String currentUser = null;
    private static int currentUserId = -1;

    public static boolean isUserLoggedIn() {
        return currentUser != null;
    }

    public static String getLoggedInUser() {
        return currentUser;
    }

    public static int getCurrentUserId() {
        return currentUserId;
    }

    public static void loginUser(String userName, int userId) {
        currentUser = userName;
        currentUserId = userId;
    }

    public static void logoutUser() {
        System.out.println("User logged out: " + currentUser);
        currentUser = null;
        currentUserId = -1;
    }

    public static boolean doesEmailExist(String email) {
        String sql = "SELECT COUNT(*) FROM Users WHERE email = ?";

        try (Connection connection = DatabaseConnector.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next() && resultSet.getInt(1) > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public static boolean addUser(String userName, String email, String password){
        if(doesEmailExist(email)){
            return false;
        }

        String sql = "INSERT INTO Users (username, email, password_hash) VALUES (?,?,?)";

        try(Connection connection = DatabaseConnector.connect();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setString(1,userName);
            preparedStatement.setString(2,email);
            preparedStatement.setString(3, password);
            preparedStatement.executeUpdate();
            System.out.println("User created: " + userName);
            return true;
        }catch (SQLException e){
            return false;
        }
    }

    public static boolean validateLogin(String userName, String password) {
        String sql = "SELECT * FROM Users WHERE username = ? AND password_hash = ?";

        try (Connection connection = DatabaseConnector.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, userName);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int userId = resultSet.getInt("id");
                loginUser(userName, userId);

                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            System.err.println("login failed" + e.getMessage());
        }
        return false;
    }

    public static String getLoggedInUserEmail() {
        if (currentUser == null) {
            return null; // Enginn skráður inn
        }

        String sql = "SELECT email FROM Users WHERE username = ?";
        try (Connection connection = DatabaseConnector.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, currentUser);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getString("email");
            }
        } catch (SQLException e) {
            System.err.println("Error fetching user email: " + e.getMessage());
        }
        return "Unknown";
    }
}
