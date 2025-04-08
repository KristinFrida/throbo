package backend;

import java.util.HashMap;
import java.util.Map;

/**
 * A mock implementation of {@code UserRepository} that simulates a storage component
 * without accessing a real database.
 */
public class UserRepositoryMock {

    /**
     * Inner class representing user data.
     */
    private static class UserData {
        private final String email;
        private final String password;

        public UserData(String email, String password) {
            this.email = email;
            this.password = password;
        }

        public String getEmail() {
            return email;
        }

        public String getPassword() {
            return password;
        }
    }

    private final Map<String, UserData> usersByUsername = new HashMap<>();
    private final Map<String, String> usersByEmail = new HashMap<>();
    private String currentUser = null;
    private int currentUserId = -1;

    /**
     * Constructor initializes the mock repository with a default test user.
     */
    public UserRepositoryMock() {
        addUser("testUser", "test@example.com", "password123");
    }

    /**
     * Validates the login credentials of a user.
     *
     * @param username The username to check.
     * @param password The password to verify.
     * @return {@code true} if the credentials are valid, otherwise {@code false}.
     */
    public boolean validateLogin(String username, String password) {
        UserData user = usersByUsername.get(username);
        if (user != null && user.getPassword().equals(password)) {
            this.currentUser = username;
            this.currentUserId = 1; // Mock user ID
            return true;
        }
        return false;
    }

    /**
     * Checks if an email is already registered in the mock database.
     *
     * @param email The email to check.
     * @return {@code true} if the email exists, otherwise {@code false}.
     */
    public boolean doesEmailExist(String email) {
        return usersByEmail.containsKey(email);
    }

    /**
     * Adds a new user to the mock storage.
     *
     * @param username The username of the new user.
     * @param email    The email of the new user.
     * @param password The password for the new user.
     * @return {@code true} if the user was successfully added, otherwise {@code false} (if the username or email is already in use).
     */
    public boolean addUser(String username, String email, String password) {
        if (usersByUsername.containsKey(username) || usersByEmail.containsKey(email)) {
            return false; // Username or email already exists
        }
        UserData newUser = new UserData(email, password);
        usersByUsername.put(username, newUser);
        usersByEmail.put(email, username);
        return true;
    }

    /**
     * Simulates logging in a user by storing their username and ID.
     *
     * @param username The username of the logged-in user.
     * @param userId   The ID of the logged-in user.
     */
    public void loginUser(String username, int userId) {
        this.currentUser = username;
        this.currentUserId = userId;
    }

    /**
     * Logs out the current user by clearing stored login details.
     */
    public void logoutUser() {
        this.currentUser = null;
        this.currentUserId = -1;
    }

    /**
     * Retrieves the username of the currently logged-in user.
     *
     * @return The username of the logged-in user, or {@code null} if no user is logged in.
     */
    public String getLoggedInUser() {
        return this.currentUser;
    }

    /**
     * Retrieves the ID of the currently logged-in user.
     *
     * @return The ID of the logged-in user, or {@code -1} if no user is logged in.
     */
    public int getCurrentUserId() {
        return this.currentUserId;
    }

    /**
     * Retrieves the email associated with the currently logged-in user.
     *
     * @return The email of the logged-in user, or {@code null} if no user is logged in.
     */
    public String getCurrentUserEmail() {
        if (this.currentUser == null) {
            return null;
        }
        UserData user = usersByUsername.get(this.currentUser);
        return (user != null) ? user.getEmail() : null;
    }
}
