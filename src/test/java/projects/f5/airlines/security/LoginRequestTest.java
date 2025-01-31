package projects.f5.airlines.security;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LoginRequestTest {

    @Test
    void testGetUsername() {
        LoginRequest loginRequest = new LoginRequest("user", "password");
        assertEquals("user", loginRequest.getUsername(), "Username should be 'user'");
    }

    @Test
    void testGetPassword() {
        LoginRequest loginRequest = new LoginRequest("user", "password");
        assertEquals("password", loginRequest.getPassword(), "Password should be 'password'");
    }

    @Test
    void testSetUsername() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("newUser");
        assertEquals("newUser", loginRequest.getUsername(), "Username should be updated to 'newUser'");
    }

    @Test
    void testSetPassword() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setPassword("newPassword");
        assertEquals("newPassword", loginRequest.getPassword(), "Password should be updated to 'newPassword'");
    }
}
