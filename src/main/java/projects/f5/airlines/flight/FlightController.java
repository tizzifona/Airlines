package projects.f5.airlines.flight;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import projects.f5.airlines.reservation.SeatUpdateDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/flights")
public class FlightController {
    private final FlightService flightService;

    public FlightController(FlightService flightService) {
        this.flightService = flightService;
    }

    @GetMapping
    public List<FlightDto> getAllFlights() {
        return flightService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<FlightDto> getFlightById(@PathVariable Long id) {
        return flightService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/seats")
    public ResponseEntity<Void> updateSeats(@PathVariable Long id, @RequestBody SeatUpdateDto seatUpdateDto) {
        try {
            flightService.updateSeats(id, seatUpdateDto);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(404).build();
        }
    }

    @PutMapping("/{id}/availability")
    public ResponseEntity<Void> updateAvailability(@PathVariable Long id,
            @RequestBody Map<String, Boolean> availability) {
        try {
            Boolean isAvailable = availability.get("isAvailable");
            if (isAvailable == null) {
                return ResponseEntity.badRequest().build();
            }
            flightService.updateAvailability(id, isAvailable);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(404).build();
        }
    }

    @GetMapping("/search")
    public List<FlightDto> searchFlights(
            @RequestParam(required = false) String departureCode,
            @RequestParam(required = false) String arrivalCode,
            @RequestParam(required = false) String departureDate,
            @RequestParam(required = false) Integer numberOfSeats) {

        LocalDateTime departureDateTime = null;
        if (departureDate != null) {
            departureDateTime = LocalDateTime.parse(departureDate);
        }

        FlightSearchDto searchDto = new FlightSearchDto(
                departureCode,
                arrivalCode,
                departureDateTime,
                numberOfSeats);

        return flightService.searchFlights(searchDto);
    }

    @PostMapping
    public FlightDto createFlight(@RequestBody FlightDto flightDto) {
        return flightService.create(flightDto);
    }
}
