package projects.f5.airlines.flight;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record FlightDto(
        Long id,
        String departureAirport,
        String arrivalAirport,
        LocalDateTime departureTime,
        LocalDateTime arrivalTime,
        Integer availableSeats,
        BigDecimal price,
        Boolean isAvailable) {
}
