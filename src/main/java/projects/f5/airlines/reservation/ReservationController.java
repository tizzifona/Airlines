package projects.f5.airlines.reservation;

import org.springframework.http.HttpStatus;
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
    private final ReservationRepository reservationRepository;

    public ReservationController(ReservationService reservationService, ReservationRepository reservationRepository) {
        this.reservationService = reservationService;
        this.reservationRepository = reservationRepository;
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
    public ResponseEntity<String> confirmReservation(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
        Long userId = securityUser.getId();

        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));

        System.out.println("Current User ID: " + userId);

        Long reservationUserId = reservation.getUser().getId();
        System.out.println("Reservation ID: " + reservation.getId());
        System.out.println("Reservation Owner ID: " + reservationUserId);

        System.out.println("Is current user ID equal to reservation owner ID? " + reservationUserId.equals(userId));

        boolean isAdmin = securityUser.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        boolean isOwner = reservationUserId.equals(userId);

        System.out.println("Is Admin: " + isAdmin);
        System.out.println("Is Owner: " + isOwner);

        if (!isAdmin && !isOwner) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not allowed to confirm this reservation.");
        }

        reservationService.confirmReservation(id);
        return ResponseEntity.ok("Reservation confirmed successfully.");
    }

}
