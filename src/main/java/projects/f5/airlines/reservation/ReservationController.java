package projects.f5.airlines.reservation;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import projects.f5.airlines.security.SecurityUser;
import java.util.List;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {
    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<Reservation> getAll() {
        return reservationService.getAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isOwner(#id)")
    public Reservation getById(@PathVariable Long id) {
        return reservationService.getById(id);
    }

    @GetMapping("/my-reservations")
    public List<Reservation> getMyReservations() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
        Long userId = securityUser.getId();

        return reservationService.findAllByUserId(userId);
    }

    @PostMapping("/create")
    public ResponseEntity<String> createReservation(@RequestBody ReservationDto reservationDto) {
        return reservationService.createReservation(reservationDto);
    }

    @PostMapping("/{id}/confirm")
    @PreAuthorize("hasRole('ADMIN')")
    public void confirmReservation(@PathVariable Long id) {
        reservationService.confirmReservation(id);
    }
}
