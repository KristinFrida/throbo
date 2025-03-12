package bakendi;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRespository {

    private String userName;
    private String password;
    private String email;

    public String getUserName(){
        return userName;
    }

    public String getEmail(){
        return email;
    }

    public static boolean doesEmailExist(String email){
        String sql = "SELECT COUNT(*) FROM Users WHERE email = ?";

        try(Connection connection = DatabaseConnector.connect();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1,email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return  resultSet.getInt(1)>0;
            }
        }catch (SQLException e){
            System.err.println("Error checking email existence" + e.getMessage());
        }
        return false;
    }

    public static boolean addUser(String userName, String email, String password){
        if(doesEmailExist(email)){
            System.err.println("this email has already been used");
            return false;
        }

        if(userName.isBlank() || email.isBlank() || password.isBlank()){
            System.err.println("Vantar upplýsingar");
            return false;
        }
        String sql = "INSERT INTO Users (username, email, password_hash) VALUES (?,?,?)";


        try(Connection connection = DatabaseConnector.connect();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setString(1,userName);
            preparedStatement.setString(2,email);
            preparedStatement.setString(3, password);
            preparedStatement.executeUpdate();
            return true;
        }catch (SQLException e){
            System.err.println("Búa til nýjan notandi failed" + e.getMessage());
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
            return resultSet.next();
        } catch (SQLException e) {
            System.err.println("login failed" + e.getMessage());
            return false;
        }
    }
}
