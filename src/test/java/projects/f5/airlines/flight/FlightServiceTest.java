package projects.f5.airlines.flight;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import projects.f5.airlines.airport.Airport;
import projects.f5.airlines.airport.AirportRepository;
import projects.f5.airlines.reservation.SeatUpdateDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FlightServiceTest {

    @InjectMocks
    private FlightService flightService;

    @Mock
    private FlightRepository flightRepository;

    @Mock
    private AirportRepository airportRepository;

    private FlightDto flightDto;
    private Flight flight;

    @BeforeEach
    void setUp() {
        flightDto = new FlightDto(1L, "JFK", "LHR", LocalDateTime.now(), LocalDateTime.now().plusHours(7),
                100, BigDecimal.valueOf(500), true);

        flight = new Flight(1L,
                new Airport(1L, "JFK", "John F. Kennedy", "New York", "USA"),
                new Airport(2L, "LHR", "London Heathrow", "London", "UK"),
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(7),
                100, true, BigDecimal.valueOf(500));
    }

    @Test
    void testCreate() {

        when(airportRepository.findByCode("JFK"))
                .thenReturn(Optional.of(new Airport(1L, "JFK", "John F. Kennedy", "New York", "USA")));
        when(airportRepository.findByCode("LHR"))
                .thenReturn(Optional.of(new Airport(2L, "LHR", "London Heathrow", "London", "UK")));

        when(flightRepository.save(any(Flight.class))).thenReturn(flight);

        FlightDto createdFlight = flightService.create(flightDto);

        assertNotNull(createdFlight);
        assertEquals(flightDto.id(), createdFlight.id());
        verify(flightRepository, times(1)).save(any(Flight.class));
    }

    @Test
    void testFindAll() {
        when(flightRepository.findAll()).thenReturn(List.of(flight));

        var flights = flightService.findAll();

        assertNotNull(flights);
        assertEquals(1, flights.size());
        assertEquals(flight.getId(), flights.get(0).id());
    }

    @Test
    void testFindById() {
        when(flightRepository.findById(1L)).thenReturn(Optional.of(flight));

        var foundFlight = flightService.findById(1L);

        assertTrue(foundFlight.isPresent());
        assertEquals(flight.getId(), foundFlight.get().id());
    }

    @Test
    void testSearchFlights() {
        FlightSearchDto searchDto = new FlightSearchDto("JFK", "LHR", LocalDateTime.now(), 1);

        when(flightRepository.findAvailableFlights(
                eq("JFK"), eq("LHR"), any(LocalDateTime.class), eq(1)))
                .thenReturn(List.of(flight));

        var flights = flightService.searchFlights(searchDto);

        assertNotNull(flights);
        assertEquals(1, flights.size());
        assertEquals(flight.getId(), flights.get(0).id());
    }

    @Test
    void testUpdateAvailability() {
        when(flightRepository.findById(1L)).thenReturn(Optional.of(flight));

        flightService.updateAvailability(1L, false);

        assertFalse(flight.getIsAvailable());
        verify(flightRepository, times(1)).save(flight);
    }

    @Test
    void testUpdateFlightStatus() {

        flightService.updateFlightStatus();
        verify(flightRepository, times(1)).updateFlightStatus(any(LocalDateTime.class));
    }

    @Test
    void testUpdateSeats() {
        SeatUpdateDto seatUpdateDto = new SeatUpdateDto(10);
        when(flightRepository.findById(1L)).thenReturn(Optional.of(flight));

        flightService.updateSeats(1L, seatUpdateDto);

        assertEquals(90, flight.getAvailableSeats());
        verify(flightRepository, times(1)).save(flight);
    }
}
