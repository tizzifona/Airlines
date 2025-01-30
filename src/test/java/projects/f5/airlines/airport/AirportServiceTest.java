package projects.f5.airlines.airport;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AirportServiceTest {

    private AirportRepository airportRepository;
    private AirportService airportService;

    @BeforeEach
    void setUp() {
        airportRepository = mock(AirportRepository.class);
        airportService = new AirportService(airportRepository);
    }

    @Test
    void testCreate() {
        AirportDto airportDto = new AirportDto(1L, "JFK", "John F. Kennedy International Airport", "New York", "USA");
        Airport airportEntity = new Airport(1L, "JFK", "John F. Kennedy International Airport", "New York", "USA");

        when(airportRepository.save(any(Airport.class))).thenReturn(airportEntity);

        AirportDto createdAirport = airportService.create(airportDto);

        assertNotNull(createdAirport);
        assertEquals(airportDto.id(), createdAirport.id());
        assertEquals(airportDto.code(), createdAirport.code());
        assertEquals(airportDto.name(), createdAirport.name());
        assertEquals(airportDto.city(), createdAirport.city());
        assertEquals(airportDto.country(), createdAirport.country());

        verify(airportRepository, times(1)).save(any(Airport.class));
    }

    @Test
    void testDelete() {

        Long airportId = 1L;

        airportService.delete(airportId);

        verify(airportRepository, times(1)).deleteById(airportId);
    }

    @Test
    void testFindAll() {

        Airport airport1 = new Airport(1L, "JFK", "John F. Kennedy International Airport", "New York", "USA");
        Airport airport2 = new Airport(2L, "LAX", "Los Angeles International Airport", "Los Angeles", "USA");

        when(airportRepository.findAll()).thenReturn(List.of(airport1, airport2));

        List<AirportDto> airportDtos = airportService.findAll();

        assertNotNull(airportDtos);
        assertEquals(2, airportDtos.size());
        assertEquals(airport1.getCode(), airportDtos.get(0).code());
        assertEquals(airport2.getCode(), airportDtos.get(1).code());

        verify(airportRepository, times(1)).findAll();
    }

    @Test
    void testFindById() {

        Long airportId = 1L;
        Airport airport = new Airport(airportId, "JFK", "John F. Kennedy International Airport", "New York", "USA");

        when(airportRepository.findById(airportId)).thenReturn(Optional.of(airport));

        Optional<AirportDto> foundAirport = airportService.findById(airportId);

        assertTrue(foundAirport.isPresent());
        assertEquals(airport.getCode(), foundAirport.get().code());

        verify(airportRepository, times(1)).findById(airportId);
    }

    @Test
    void testUpdate() {

        Long airportId = 1L;
        AirportDto updatedAirportDto = new AirportDto(1L, "JFK", "Updated Airport", "New York", "USA");
        Airport existingAirport = new Airport(airportId, "JFK", "Old Airport", "New York", "USA");
        Airport updatedAirport = new Airport(airportId, "JFK", "Updated Airport", "New York", "USA");

        when(airportRepository.findById(airportId)).thenReturn(Optional.of(existingAirport));
        when(airportRepository.save(existingAirport)).thenReturn(updatedAirport);

        AirportDto result = airportService.update(airportId, updatedAirportDto);

        assertNotNull(result);
        assertEquals(updatedAirportDto.name(), result.name());
        assertEquals(updatedAirportDto.city(), result.city());

        verify(airportRepository, times(1)).findById(airportId);
        verify(airportRepository, times(1)).save(existingAirport);
    }

    @Test
    void testUpdateAirportNotFound() {

        Long airportId = 1L;
        AirportDto updatedAirportDto = new AirportDto(1L, "JFK", "Updated Airport", "New York", "USA");

        when(airportRepository.findById(airportId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> airportService.update(airportId, updatedAirportDto));
        assertEquals("Airport not found", exception.getMessage());
    }
}
