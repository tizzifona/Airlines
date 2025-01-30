package projects.f5.airlines.airport;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AirportControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AirportService airportService;

    @InjectMocks
    private AirportController airportController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(airportController).build();
    }

    @Test
    void testCreateAirport() throws Exception {
        AirportDto airportDto = new AirportDto(1L, "JFK", "John F. Kennedy", "New York", "USA");
        when(airportService.create(any(AirportDto.class))).thenReturn(airportDto);

        mockMvc.perform(post("/api/airports")
                .contentType("application/json")
                .content(
                        "{\"id\": 1, \"code\": \"JFK\", \"name\": \"John F. Kennedy\", \"city\": \"New York\", \"country\": \"USA\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.code").value("JFK"))
                .andExpect(jsonPath("$.name").value("John F. Kennedy"))
                .andExpect(jsonPath("$.city").value("New York"))
                .andExpect(jsonPath("$.country").value("USA"));

        verify(airportService, times(1)).create(any(AirportDto.class));
    }

    @Test
    void testDeleteAirport() throws Exception {
        Long airportId = 1L;
        doNothing().when(airportService).delete(airportId);

        mockMvc.perform(delete("/api/airports/{id}", airportId))
                .andExpect(status().isNoContent());

        verify(airportService, times(1)).delete(airportId);
    }

    @Test
    void testGetAirportById() throws Exception {
        Long airportId = 1L;
        AirportDto airportDto = new AirportDto(1L, "JFK", "John F. Kennedy", "New York", "USA");
        when(airportService.findById(airportId)).thenReturn(Optional.of(airportDto));

        mockMvc.perform(get("/api/airports/{id}", airportId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.code").value("JFK"))
                .andExpect(jsonPath("$.name").value("John F. Kennedy"))
                .andExpect(jsonPath("$.city").value("New York"))
                .andExpect(jsonPath("$.country").value("USA"));

        verify(airportService, times(1)).findById(airportId);
    }

    @Test
    void testGetAirportByIdNotFound() throws Exception {
        Long airportId = 1L;
        when(airportService.findById(airportId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/airports/{id}", airportId))
                .andExpect(status().isNotFound());

        verify(airportService, times(1)).findById(airportId);
    }

    @Test
    void testGetAllAirports() throws Exception {
        List<AirportDto> airports = List.of(
                new AirportDto(1L, "JFK", "John F. Kennedy", "New York", "USA"),
                new AirportDto(2L, "LAX", "Los Angeles International", "Los Angeles", "USA"));
        when(airportService.findAll()).thenReturn(airports);

        mockMvc.perform(get("/api/airports"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));

        verify(airportService, times(1)).findAll();
    }

    @Test
    void testUpdateAirport() throws Exception {
        Long airportId = 1L;
        AirportDto airportDto = new AirportDto(1L, "JFK", "John F. Kennedy", "New York", "USA");
        when(airportService.update(eq(airportId), any(AirportDto.class))).thenReturn(airportDto);

        mockMvc.perform(put("/api/airports/{id}", airportId)
                .contentType("application/json")
                .content(
                        "{\"id\": 1, \"code\": \"JFK\", \"name\": \"John F. Kennedy\", \"city\": \"New York\", \"country\": \"USA\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.code").value("JFK"))
                .andExpect(jsonPath("$.name").value("John F. Kennedy"))
                .andExpect(jsonPath("$.city").value("New York"))
                .andExpect(jsonPath("$.country").value("USA"));

        verify(airportService, times(1)).update(eq(airportId), any(AirportDto.class));
    }

    @Test
    void testUpdateAirportNotFound() throws Exception {
        Long airportId = 1L;
        when(airportService.update(eq(airportId), any(AirportDto.class))).thenThrow(new RuntimeException());

        mockMvc.perform(put("/api/airports/{id}", airportId)
                .contentType("application/json")
                .content(
                        "{\"id\": 1, \"code\": \"JFK\", \"name\": \"John F. Kennedy\", \"city\": \"New York\", \"country\": \"USA\"}"))
                .andExpect(status().isNotFound());

        verify(airportService, times(1)).update(eq(airportId), any(AirportDto.class));
    }
}
