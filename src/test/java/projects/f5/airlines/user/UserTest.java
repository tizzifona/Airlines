package projects.f5.airlines.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import projects.f5.airlines.reservation.Reservation;
import projects.f5.airlines.role.UserRole;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {
    private User user;

    @BeforeEach
    void setUp() {
        Set<UserRole> roles = new HashSet<>();
        Set<Reservation> reservations = new HashSet<>();
        user = new User(1L, "testUser", "password123", "test@example.com", "profile.jpg", roles, reservations);
    }

    @Test
    void testGetEmail() {
        assertEquals("test@example.com", user.getEmail());
    }

    @Test
    void testGetId() {
        assertEquals(1L, user.getId());
    }

    @Test
    void testGetPassword() {
        assertEquals("password123", user.getPassword());
    }

    @Test
    void testGetProfileImage() {
        assertEquals("profile.jpg", user.getProfileImage());
    }

    @Test
    void testGetReservations() {
        assertNotNull(user.getReservations());
        assertTrue(user.getReservations().isEmpty());
    }

    @Test
    void testGetRoles() {
        assertNotNull(user.getRoles());
        assertTrue(user.getRoles().isEmpty());
    }

    @Test
    void testGetUsername() {
        assertEquals("testUser", user.getUsername());
    }

    @Test
    void testSetEmail() {
        user.setEmail("new@example.com");
        assertEquals("new@example.com", user.getEmail());
    }

    @Test
    void testSetId() {
        user.setId(2L);
        assertEquals(2L, user.getId());
    }

    @Test
    void testSetPassword() {
        user.setPassword("newPass");
        assertEquals("newPass", user.getPassword());
    }

    @Test
    void testSetProfileImage() {
        user.setProfileImage("newImage.jpg");
        assertEquals("newImage.jpg", user.getProfileImage());
    }

    @Test
    void testSetReservations() {
        Set<Reservation> newReservations = new HashSet<>();
        user.setReservations(newReservations);
        assertEquals(newReservations, user.getReservations());
    }

    @Test
    void testSetRoles() {
        Set<UserRole> newRoles = new HashSet<>();
        user.setRoles(newRoles);
        assertEquals(newRoles, user.getRoles());
    }

    @Test
    void testSetUsername() {
        user.setUsername("newUser");
        assertEquals("newUser", user.getUsername());
    }
}