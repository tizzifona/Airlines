package projects.f5.airlines.flight;

import java.time.LocalDateTime;

public record FlightSearchDto(
        String departureAirport,
        String arrivalAirport,
        LocalDateTime departureDate,
        Integer numberOfSeats) {
}
