package projects.f5.airlines.reservation;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import projects.f5.airlines.flight.Flight;
import projects.f5.airlines.user.User;

class ReservationTest {

    private Reservation reservation;
    private User user;
    private Flight flight;
    private LocalDateTime now;

    @BeforeEach
    void setUp() {
        user = new User();
        flight = new Flight();
        now = LocalDateTime.now();

        reservation = new Reservation(user, flight, 2, new BigDecimal("500.00"),
                ReservationStatus.CONFIRMED, now, now.plusDays(1));
    }

    @Test
    void testGetCreatedAt() {
        reservation.onCreate();
        assertThat(reservation.getCreatedAt()).isNotNull();
    }

    @Test
    void testGetExpirationTime() {
        assertThat(reservation.getExpirationTime()).isEqualTo(now.plusDays(1));
    }

    @Test
    void testGetFlight() {
        assertThat(reservation.getFlight()).isEqualTo(flight);
    }

    @Test
    void testGetId() {
        reservation.setId(1L);
        assertThat(reservation.getId()).isEqualTo(1L);
    }

    @Test
    void testGetReservationTime() {
        assertThat(reservation.getReservationTime()).isEqualTo(now);
    }

    @Test
    void testGetSeatsReserved() {
        assertThat(reservation.getSeatsReserved()).isEqualTo(2);
    }

    @Test
    void testGetStatus() {
        assertThat(reservation.getStatus()).isEqualTo(ReservationStatus.CONFIRMED);
    }

    @Test
    void testGetTotalPrice() {
        assertThat(reservation.getTotalPrice()).isEqualByComparingTo(new BigDecimal("500.00"));
    }

    @Test
    void testGetUpdatedAt() {
        reservation.onCreate();
        assertThat(reservation.getUpdatedAt()).isNotNull();
    }

    @Test
    void testGetUser() {
        assertThat(reservation.getUser()).isEqualTo(user);
    }

    @Test
    void testOnCreate() {
        reservation.onCreate();
        assertThat(reservation.getCreatedAt()).isNotNull();
        assertThat(reservation.getUpdatedAt()).isNotNull();
    }

    @Test
    void testOnUpdate() {
        reservation.onCreate();
        LocalDateTime beforeUpdate = reservation.getUpdatedAt();
        reservation.onUpdate();
        assertThat(reservation.getUpdatedAt()).isAfter(beforeUpdate);
    }

    @Test
    void testSetCreatedAt() {
        LocalDateTime createdAt = LocalDateTime.now().minusDays(1);
        reservation.setCreatedAt(createdAt);
        assertThat(reservation.getCreatedAt()).isEqualTo(createdAt);
    }

    @Test
    void testSetExpirationTime() {
        LocalDateTime expiration = now.plusDays(2);
        reservation.setExpirationTime(expiration);
        assertThat(reservation.getExpirationTime()).isEqualTo(expiration);
    }

    @Test
    void testSetFlight() {
        Flight newFlight = new Flight();
        reservation.setFlight(newFlight);
        assertThat(reservation.getFlight()).isEqualTo(newFlight);
    }

    @Test
    void testSetId() {
        reservation.setId(10L);
        assertThat(reservation.getId()).isEqualTo(10L);
    }

    @Test
    void testSetReservationTime() {
        LocalDateTime newTime = now.minusHours(1);
        reservation.setReservationTime(newTime);
        assertThat(reservation.getReservationTime()).isEqualTo(newTime);
    }

    @Test
    void testSetSeatsReserved() {
        reservation.setSeatsReserved(5);
        assertThat(reservation.getSeatsReserved()).isEqualTo(5);
    }

    @Test
    void testSetStatus() {
        reservation.setStatus(ReservationStatus.CANCELLED);
        assertThat(reservation.getStatus()).isEqualTo(ReservationStatus.CANCELLED);
    }

    @Test
    void testSetTotalPrice() {
        reservation.setTotalPrice(new BigDecimal("800.00"));
        assertThat(reservation.getTotalPrice()).isEqualByComparingTo(new BigDecimal("800.00"));
    }

    @Test
    void testSetUpdatedAt() {
        LocalDateTime newUpdatedAt = now.plusHours(2);
        reservation.setUpdatedAt(newUpdatedAt);
        assertThat(reservation.getUpdatedAt()).isEqualTo(newUpdatedAt);
    }

    @Test
    void testSetUser() {
        User newUser = new User();
        reservation.setUser(newUser);
        assertThat(reservation.getUser()).isEqualTo(newUser);
    }
}