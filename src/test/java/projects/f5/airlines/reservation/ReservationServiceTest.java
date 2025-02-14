package projects.f5.airlines.reservation;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import projects.f5.airlines.flight.Flight;
import projects.f5.airlines.flight.FlightRepository;
import projects.f5.airlines.security.SecurityUser;
import projects.f5.airlines.user.User;
import projects.f5.airlines.user.UserRepository;

@ExtendWith(MockitoExtension.class)
public class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private FlightRepository flightRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private Random random;

    @Mock
    private SecurityUser securityUser;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private ReservationService reservationService;

    private User user;
    private Flight flight;
    private Reservation reservation;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("testuser");

        flight = new Flight();
        flight.setId(1L);
        flight.setAvailableSeats(10);
        flight.setPrice(BigDecimal.valueOf(100));

        reservation = new Reservation(user, flight, 2, BigDecimal.valueOf(200),
                ReservationStatus.PENDING, LocalDateTime.now(), LocalDateTime.now().plusMinutes(15));
    }

    @Test
    void testGetAll() {
        when(reservationRepository.findAll()).thenReturn(List.of(reservation));
        List<Reservation> result = reservationService.getAll();
        assertEquals(1, result.size());
    }

    @Test
    void testGetById() {
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));
        Reservation result = reservationService.getById(1L);
        assertNotNull(result);
        assertEquals(reservation, result);
    }

    @Test
    void testFindAllByUserId() {
        when(reservationRepository.findByUserId(1L)).thenReturn(List.of(reservation));
        List<Reservation> result = reservationService.findAllByUserId(1L);
        assertEquals(1, result.size());
    }

    @SuppressWarnings("deprecation")
    @Test
    void testCreateReservation() {
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getName()).thenReturn("testuser");
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(flightRepository.findById(1L)).thenReturn(Optional.of(flight));

        lenient().when(random.nextDouble()).thenReturn(0.5);

        ReservationDto dto = new ReservationDto(1L, 2);
        ResponseEntity<String> response = reservationService.createReservation(dto);

        assertEquals(201, response.getStatusCodeValue());
        assertTrue(response.getBody().contains("Reservation created successfully"));
    }

    @Test
    void testConfirmReservation() {
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));
        reservationService.confirmReservation(1L);
        assertEquals(ReservationStatus.CONFIRMED, reservation.getStatus());
        verify(reservationRepository, times(1)).save(reservation);
    }

    @Test
    void testGetRandomPricePerSeat() {
        lenient().when(random.nextDouble()).thenReturn(0.5);
        BigDecimal price = reservationService.getRandomPricePerSeat();
        assertNotNull(price);
        assertTrue(price.compareTo(BigDecimal.valueOf(50)) >= 0 && price.compareTo(BigDecimal.valueOf(500)) <= 0);
    }

    @Test
    void deleteReservation_Success() {
        Long reservationId = 1L;
        User user = new User();
        user.setId(10L);

        Reservation reservation = new Reservation();
        reservation.setId(reservationId);
        reservation.setUser(user);

        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(authentication.getPrincipal()).thenReturn(securityUser);
        when(securityUser.getId()).thenReturn(10L);
        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservation));

        ResponseEntity<Map<String, String>> response = reservationService.deleteReservation(reservationId);

        verify(reservationRepository, times(1)).delete(reservation);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Reservation deleted successfully.", response.getBody().get("message"));
    }

    @Test
    void deleteReservation_Forbidden() {
        Long reservationId = 1L;
        User owner = new User();
        owner.setId(10L);

        Reservation reservation = new Reservation();
        reservation.setId(reservationId);
        reservation.setUser(owner);

        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(authentication.getPrincipal()).thenReturn(securityUser);
        when(securityUser.getId()).thenReturn(20L);
        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservation));

        ResponseEntity<Map<String, String>> response = reservationService.deleteReservation(reservationId);

        verify(reservationRepository, never()).delete(any());
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("You are not allowed to delete this reservation.", response.getBody().get("error"));
    }

    @Test
    void deleteReservation_NotFound() {
        Long reservationId = 1L;
        Mockito.when(reservationRepository.findById(reservationId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            reservationService.deleteReservation(reservationId);
        });

        assertEquals("Reservation not found", exception.getMessage());
    }

}
