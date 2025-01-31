package projects.f5.airlines.flight;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import projects.f5.airlines.reservation.SeatUpdateDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class FlightControllerTest {

        @Mock
        private FlightService flightService;

        @InjectMocks
        private FlightController flightController;

        private MockMvc mockMvc;

        @BeforeEach
        void setup() {
                mockMvc = MockMvcBuilders.standaloneSetup(flightController).build();
        }

        @Test
        void testCreateFlight() throws Exception {
                FlightDto flightDto = new FlightDto(1L, "JFK", "LAX", LocalDateTime.now(),
                                LocalDateTime.now().plusHours(5),
                                150, BigDecimal.valueOf(299.99), true);

                when(flightService.create(any(FlightDto.class))).thenReturn(flightDto);

                mockMvc.perform(post("/api/flights")
                                .contentType("application/json")
                                .content(
                                                "{\"departureAirport\": \"JFK\", \"arrivalAirport\": \"LAX\", \"departureTime\": \"2025-01-30T10:00:00\", \"arrivalTime\": \"2025-01-30T15:00:00\", \"availableSeats\": 150, \"price\": 299.99, \"isAvailable\": true}"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value(1L))
                                .andExpect(jsonPath("$.departureAirport").value("JFK"))
                                .andExpect(jsonPath("$.arrivalAirport").value("LAX"));
        }

        @Test
        void testGetAllFlights() throws Exception {
                FlightDto flightDto = new FlightDto(1L, "JFK", "LAX", LocalDateTime.now(),
                                LocalDateTime.now().plusHours(5),
                                150, BigDecimal.valueOf(299.99), true);

                when(flightService.findAll()).thenReturn(Collections.singletonList(flightDto));

                mockMvc.perform(get("/api/flights"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$[0].id").value(1L))
                                .andExpect(jsonPath("$[0].departureAirport").value("JFK"));
        }

        @Test
        void testGetFlightById() throws Exception {
                FlightDto flightDto = new FlightDto(1L, "JFK", "LAX", LocalDateTime.now(),
                                LocalDateTime.now().plusHours(5),
                                150, BigDecimal.valueOf(299.99), true);

                when(flightService.findById(1L)).thenReturn(Optional.of(flightDto));

                mockMvc.perform(get("/api/flights/1"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value(1L))
                                .andExpect(jsonPath("$.departureAirport").value("JFK"))
                                .andExpect(jsonPath("$.arrivalAirport").value("LAX"));
        }

        @Test
        void testGetFlightByIdNotFound() throws Exception {
                when(flightService.findById(anyLong())).thenReturn(Optional.empty());

                mockMvc.perform(get("/api/flights/999"))
                                .andExpect(status().isNotFound());
        }

        @Test
        void testSearchFlights() throws Exception {
                FlightDto flightDto = new FlightDto(1L, "JFK", "LAX", LocalDateTime.now(),
                                LocalDateTime.now().plusHours(5),
                                150, BigDecimal.valueOf(299.99), true);

                when(flightService.searchFlights(any(FlightSearchDto.class)))
                                .thenReturn(Collections.singletonList(flightDto));

                mockMvc.perform(get("/api/flights/search")
                                .param("departureCode", "JFK")
                                .param("arrivalCode", "LAX")
                                .param("departureDate", "2025-01-30T10:00:00")
                                .param("numberOfSeats", "150"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$[0].departureAirport").value("JFK"))
                                .andExpect(jsonPath("$[0].arrivalAirport").value("LAX"));
        }

        @Test
        void testUpdateAvailability() throws Exception {

                doNothing().when(flightService).updateAvailability(anyLong(), any(Boolean.class));

                mockMvc.perform(put("/api/flights/1/availability")
                                .contentType("application/json")
                                .content("{\"isAvailable\": true}"))
                                .andExpect(status().isOk());
        }

        @Test
        void testUpdateAvailabilityBadRequest() throws Exception {
                mockMvc.perform(put("/api/flights/1/availability")
                                .contentType("application/json")
                                .content("{\"isAvailable\": null}"))
                                .andExpect(status().isBadRequest());
        }

        @Test
        void testUpdateSeats() throws Exception {

                doNothing().when(flightService).updateSeats(anyLong(), any(SeatUpdateDto.class));

                mockMvc.perform(put("/api/flights/1/seats")
                                .contentType("application/json")
                                .content("{\"availableSeats\": 100}"))
                                .andExpect(status().isOk());
        }

        @Test
        void testUpdateSeatsNotFound() throws Exception {

                doThrow(new RuntimeException("Flight not found")).when(flightService).updateSeats(anyLong(),
                                any(SeatUpdateDto.class));

                mockMvc.perform(put("/api/flights/999/seats")
                                .contentType("application/json")
                                .content("{\"availableSeats\": 100}"))
                                .andExpect(status().isNotFound());
        }

        @Test
        public void testDeleteFlight_Success() throws Exception {
                Long flightId = 1L;
                Mockito.when(flightService.delete(flightId)).thenReturn(true);

                mockMvc.perform(delete("/api/flights/{id}", flightId)
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.message").value("Flight deleted successfully"));
        }

        @Test
        public void testDeleteFlight_NotFound() throws Exception {
                Long flightId = 2L;
                Mockito.when(flightService.delete(flightId)).thenReturn(false);

                mockMvc.perform(delete("/api/flights/{id}", flightId)
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isNotFound())
                                .andExpect(jsonPath("$.error").value("Flight not found"));
        }

}
