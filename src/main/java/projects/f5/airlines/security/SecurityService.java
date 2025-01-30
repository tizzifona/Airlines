package projects.f5.airlines.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import projects.f5.airlines.reservation.Reservation;
import projects.f5.airlines.reservation.ReservationRepository;

@Service
public class SecurityService {

    private final ReservationRepository reservationRepository;

    public SecurityService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public boolean isOwner(Long reservationId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
        Long userId = securityUser.getId();

        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));

        return reservation.getId().equals(userId);
    }
}
