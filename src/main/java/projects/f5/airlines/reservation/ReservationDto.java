package projects.f5.airlines.reservation;

public record ReservationDto(
        Long flightId,
        Integer numberOfSeats) {
}
