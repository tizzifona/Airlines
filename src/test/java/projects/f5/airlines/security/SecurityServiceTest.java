package projects.f5.airlines.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import projects.f5.airlines.reservation.Reservation;
import projects.f5.airlines.reservation.ReservationRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SecurityServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private SecurityService securityService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void testIsOwner_whenReservationBelongsToUser() {
        Long reservationId = 1L;
        Long userId = 1L;

        SecurityUser securityUser = mock(SecurityUser.class);
        when(securityUser.getId()).thenReturn(userId);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(securityUser);

        Reservation reservation = mock(Reservation.class);
        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservation));
        when(reservation.getId()).thenReturn(userId);

        assertTrue(securityService.isOwner(reservationId));
    }

    @Test
    void testIsOwner_whenReservationDoesNotBelongToUser() {
        Long reservationId = 1L;
        Long userId = 2L;

        SecurityUser securityUser = mock(SecurityUser.class);
        when(securityUser.getId()).thenReturn(userId);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(securityUser);

        Reservation reservation = mock(Reservation.class);
        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservation));
        when(reservation.getId()).thenReturn(1L);

        assertFalse(securityService.isOwner(reservationId));
    }

    @Test
    void testIsOwner_whenReservationNotFound() {
        Long reservationId = 1L;
        Long userId = 1L;

        SecurityUser securityUser = mock(SecurityUser.class);
        when(securityUser.getId()).thenReturn(userId);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(securityUser);

        when(reservationRepository.findById(reservationId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> securityService.isOwner(reservationId));
    }
}
