package projects.f5.airlines.airport;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/airports")
@PreAuthorize("hasRole('ADMIN')")
public class AirportController {
    private final AirportService airportService;

    public AirportController(AirportService airportService) {
        this.airportService = airportService;
    }

    @GetMapping
    public List<AirportDto> getAllAirports() {
        return airportService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AirportDto> getAirportById(@PathVariable Long id) {
        return airportService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public AirportDto createAirport(@RequestBody AirportDto airportDto) {
        return airportService.create(airportDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AirportDto> updateAirport(@PathVariable Long id, @RequestBody AirportDto airportDto) {
        try {
            return ResponseEntity.ok(airportService.update(id, airportDto));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAirport(@PathVariable Long id) {
        airportService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
