package projects.f5.airlines.flight;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import projects.f5.airlines.airport.Airport;

@Service
public class FlightService {

    private final FlightRepository flightRepository;

    public FlightService(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
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

    public List<FlightDto> searchFlights(Airport departure, Airport arrival, LocalDateTime date, int seats) {
        return flightRepository.findAll().stream()
                .filter(flight -> flight.getDepartureAirport().equals(departure) &&
                        flight.getArrivalAirport().equals(arrival) &&
                        flight.getDepartureTime().toLocalDate().equals(date.toLocalDate()) &&
                        flight.getAvailableSeats() >= seats)
                .map(this::convertToDto)
                .toList();
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

    public void updateSeats(Long flightId, int bookedSeats) {
        flightRepository.findById(flightId).ifPresent(flight -> {
            int remainingSeats = flight.getAvailableSeats() - bookedSeats;
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
        Airport departureAirport = new Airport();
        departureAirport.setCode(flightDto.departureAirport());

        Airport arrivalAirport = new Airport();
        arrivalAirport.setCode(flightDto.arrivalAirport());

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
