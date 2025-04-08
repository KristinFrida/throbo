package backend;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserControllerTest {

    private UserRepositoryMock mockUserRepository;

    @BeforeEach
    void setUp() {
        mockUserRepository = new UserRepositoryMock();
    }

    @AfterEach
    void tearDown() {
        mockUserRepository.logoutUser(); // Reset login state after each test
    }

    @Test
    void testSuccessfulLogin() {
        boolean result = mockUserRepository.validateLogin("testUser", "password123");
        assertTrue(result, "Login should succeed with correct username and password");
    }

    @Test
    void testFailedLogin_WrongPassword() {
        boolean result = mockUserRepository.validateLogin("testUser", "wrongPass");
        assertFalse(result, "Login should fail with incorrect password");
    }

    @Test
    void testRegisterNewUser() {
        boolean result = mockUserRepository.addUser("newUser", "newUser@test.com", "securePass");
        assertTrue(result, "Registration should succeed for a new user");
    }

    @Test
    void testRegisterExistingUsername() {
        boolean result = mockUserRepository.addUser("testUser", "newEmail@test.com", "password123");
        assertFalse(result, "Should not allow registration with an existing username");
    }

    @Test
    void testRegisterExistingEmail() {
        boolean result = mockUserRepository.addUser("newUser2", "test@example.com", "newPass");
        assertFalse(result, "Should not allow registration with an already registered email");
    }

    @Test
    void testLogout() {
        mockUserRepository.loginUser("testUser", 1);
        mockUserRepository.logoutUser();
        assertNull(mockUserRepository.getLoggedInUser(), "User should be logged out");
    }
}
