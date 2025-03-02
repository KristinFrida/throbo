package vidmot;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JavaPostgreSql {


    public static void writeToDatabase(String userName, String userPassword, String userEmail){


        // er með þetta á tveimur stöðum þarf að taka það í burta frá öðrum, en þarf að finna fyrst betri leið af því
        String url = "jdbc:postgresql://localhost:5432/users_for_website";
        String user = "postgres";
        String password = "1234";

        String name = userName;
        String passw = userPassword;
        String email = userEmail;

        String query = "INSERT INTO users(username, password, email) VALUES(?, ?, ?)";

        try (Connection con = DriverManager.getConnection(url,user,password);
             PreparedStatement pst = con.prepareStatement(query)) {

            pst.setString(1, name);
            pst.setString(2, passw);
            pst.setString(3, email);
            pst.executeUpdate();
            System.out.println("Sucessfully signed up user");


        }catch (SQLException ex) {
            Logger lgr = Logger.getLogger(JavaPostgreSql.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }



    }
}
