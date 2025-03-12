package bakendi;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static bakendi.UserRespository.addUser;
import static bakendi.UserRespository.validateLogin;

public class DatabaseConnector {
    private static final String URL = "jdbc:sqlite:userDataBase.db";

    public static Connection connect() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(URL);
            System.out.println("Connected to SQLite database.");
        } catch (SQLException e) {
            System.err.println("Connection failed: " + e.getMessage());
        }
        return connection;
    }

    public static void main(String[] args) {
        Connection conn = connect();
        if (conn != null) {
            System.out.println("SQLite connection works!");
        }
    }
}
