package projects.f5.airlines.airport;

public record AirportDto(
        Long id,
        String code,
        String name,
        String city,
        String country) {
}
