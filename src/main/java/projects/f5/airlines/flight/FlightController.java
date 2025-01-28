package projects.f5.airlines.flight;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

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

    // @GetMapping("/search")
    // public List<FlightDto> searchFlights(
    // @RequestParam(required = false) String departureCode,
    // @RequestParam(required = false) String arrivalCode,
    // @RequestParam(required = false) LocalDateTime date,
    // @RequestParam(required = false, defaultValue = "1") int seats) {

    // Airport departure = null;
    // if (departureCode != null) {
    // departure = new Airport();
    // departure.setCode(departureCode);
    // }

    // Airport arrival = null;
    // if (arrivalCode != null) {
    // arrival = new Airport();
    // arrival.setCode(arrivalCode);
    // }
    // return flightService.searchFlights(departure, arrival, date, seats);
    // }

    // @GetMapping("/search")
    // public List<FlightDto> searchFlights(
    // @RequestParam(required = false) String departureCode,
    // @RequestParam(required = false) String arrivalCode,
    // @RequestParam(required = false) LocalDateTime date,
    // @RequestParam(required = false, defaultValue = "1") int seats) {

    // // Создаем объект FlightSearchDto
    // FlightSearchDto searchDto = new FlightSearchDto(
    // departureCode, // Код аэропорта отправления
    // arrivalCode, // Код аэропорта прибытия
    // date, // Дата вылета
    // seats // Количество мест
    // );

    // // Передаем объект searchDto в метод сервиса
    // return flightService.searchFlights(searchDto);
    // }

    @GetMapping("/search")
    public List<FlightDto> searchFlights(
            @RequestParam(required = false) String departureCode,
            @RequestParam(required = false) String arrivalCode,
            @RequestParam(required = false) String departureDate,
            @RequestParam(required = false) Integer numberOfSeats) {

        LocalDateTime departureDateTime = null;
        if (departureDate != null) {
            departureDateTime = LocalDateTime.parse(departureDate); // ISO-8601 format
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
