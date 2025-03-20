package bakendi;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserControllerTest {

    private UserRepositoryMock mockUserRepository;

    @BeforeEach
    void setUp() {
        mockUserRepository = new UserRepositoryMock();
    }

    @Test
    void testSuccessfulLogin() {
        boolean result = mockUserRepository.validateLogin("testUser", "password123");
        assertTrue(result, "Innskráning átti að virka fyrir rétt notandanafn og lykilorð");
    }

    @Test
    void testFailedLogin_WrongPassword() {
        boolean result = mockUserRepository.validateLogin("testUser", "wrongPass");
        assertFalse(result, "Innskráning ætti að mistakast með vitlaust lykilorð");
    }

    @Test
    void testRegisterNewUser() {
        boolean result = mockUserRepository.addUser("newUser", "newUser@test.com", "securePass");
        assertTrue(result, "Skráning átti að heppnast fyrir nýjan notanda");
    }

    @Test
    void testRegisterExistingUser() {
        boolean result = mockUserRepository.addUser("testUser", "test@test.com", "password123");
        assertFalse(result, "Ekki á að vera hægt að skrá notanda sem er þegar til");
    }

    @Test
    void testLogout() {
        mockUserRepository.loginUser("testUser", 1);
        mockUserRepository.logoutUser();
        assertNull(mockUserRepository.getLoggedInUser(), "Notandi ætti að vera útskráður");
    }
}
