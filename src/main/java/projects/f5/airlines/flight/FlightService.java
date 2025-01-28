package projects.f5.airlines.flight;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import projects.f5.airlines.airport.Airport;
import projects.f5.airlines.airport.AirportRepository;
import projects.f5.airlines.reservation.SeatUpdateDto;

@Service
public class FlightService {

    private final FlightRepository flightRepository;
    private final AirportRepository airportRepository;

    public FlightService(FlightRepository flightRepository, AirportRepository airportRepository) {
        this.flightRepository = flightRepository;
        this.airportRepository = airportRepository;
    }

    public List<FlightDto> findAll() {
        return flightRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public Optional<FlightDto> findById(Long id) {
        return flightRepository.findById(id)
                .map(this::convertToDto);
    }

    public List<FlightDto> searchFlights(FlightSearchDto searchDto) {
        return flightRepository.findAvailableFlights(
                searchDto.departureAirport(),
                searchDto.arrivalAirport(),
                searchDto.departureDate(),
                searchDto.numberOfSeats()).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Scheduled(fixedRate = 60000)
    public void updateFlightStatus() {
        LocalDateTime now = LocalDateTime.now();
        flightRepository.updateFlightStatus(now);
    }

    public FlightDto create(FlightDto flightDto) {
        Flight flight = convertToEntity(flightDto);
        Flight savedFlight = flightRepository.save(flight);
        return convertToDto(savedFlight);
    }

    public void updateAvailability(Long flightId, boolean isAvailable) {
        flightRepository.findById(flightId).ifPresent(flight -> {
            flight.setIsAvailable(isAvailable);
            flightRepository.save(flight);
        });
    }

    public void updateSeats(Long flightId, SeatUpdateDto seatUpdateDto) {
        flightRepository.findById(flightId).ifPresent(flight -> {
            int remainingSeats = flight.getAvailableSeats() - seatUpdateDto.getBookedSeats();
            flight.setAvailableSeats(remainingSeats);
            if (remainingSeats <= 0) {
                flight.setIsAvailable(false);
            }
            flightRepository.save(flight);
        });
    }

    private FlightDto convertToDto(Flight flight) {
        return new FlightDto(
                flight.getId(),
                flight.getDepartureAirport().getCode(),
                flight.getArrivalAirport().getCode(),
                flight.getDepartureTime(),
                flight.getArrivalTime(),
                flight.getAvailableSeats(),
                flight.getPrice(),
                flight.getIsAvailable());
    }

    private Flight convertToEntity(FlightDto flightDto) {
        Airport departureAirport = airportRepository.findByCode(flightDto.departureAirport())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Departure airport not found: " + flightDto.departureAirport()));

        Airport arrivalAirport = airportRepository.findByCode(flightDto.arrivalAirport())
                .orElseThrow(
                        () -> new IllegalArgumentException("Arrival airport not found: " + flightDto.arrivalAirport()));

        return new Flight(
                flightDto.id(),
                departureAirport,
                arrivalAirport,
                flightDto.departureTime(),
                flightDto.arrivalTime(),
                flightDto.availableSeats(),
                flightDto.isAvailable(),
                flightDto.price());
    }
}
