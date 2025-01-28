package projects.f5.airlines.reservation;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import projects.f5.airlines.user.User;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {
    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/my")
    public List<Reservation> getMyReservations(@RequestAttribute("user") User user) {
        return reservationService.findAllByUserId(user.getId());
    }

    @PostMapping
    public ReservationDto createReservation(
            @RequestAttribute("user") User user,
            @RequestParam Long flightId,
            @RequestParam int seats) {

        ReservationDto reservationDto = new ReservationDto(flightId, seats);
        return reservationService.create(user.getId(), flightId, reservationDto);
    }

    @PostMapping("/{id}/confirm")
    @PreAuthorize("hasRole('ADMIN')")
    public void confirmReservation(@PathVariable Long id) {
        reservationService.confirmReservation(id);
    }
}
