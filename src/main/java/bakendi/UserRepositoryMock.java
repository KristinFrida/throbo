package bakendi;

import java.util.HashMap;
import java.util.Map;

/**
 * Mock útgáfa af UserRepository sem hermir eftir gagnageymslu án aðgangs að raunverulegum gagnagrunni.
 */
public class UserRepositoryMock {

    private final Map<String, String> users = new HashMap<>();
    private String currentUser = null;
    private int currentUserId = -1;

    public UserRepositoryMock() {
        // Setjum inn einn skráðann notanda til að prófanir virki
        users.put("testUser", "password123");
    }

    public boolean validateLogin(String username, String password) {
        if (users.containsKey(username) && users.get(username).equals(password)) {
            this.currentUser = username;
            this.currentUserId = 1;
            return true;
        }
        return false;
    }

    public boolean addUser(String username, String email, String password) {
        if (users.containsKey(username)) {
            return false;
        }
        users.put(username, password);
        return true;
    }

    public void loginUser(String username, int userId) {
        this.currentUser = username;
        this.currentUserId = userId;
    }

    public void logoutUser() {
        this.currentUser = null;
        this.currentUserId = -1;
    }

    public String getLoggedInUser() {
        return this.currentUser;
    }

}
