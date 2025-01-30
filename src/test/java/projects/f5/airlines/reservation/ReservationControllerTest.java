package projects.f5.airlines.reservation;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import projects.f5.airlines.security.SecurityUser;

import java.util.List;
import java.util.Arrays;

@ExtendWith(MockitoExtension.class)
class ReservationControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ReservationService reservationService;

    @InjectMocks
    private ReservationController reservationController;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(reservationController).build();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetAll() throws Exception {
        when(reservationService.getAll()).thenReturn(Arrays.asList(new Reservation(), new Reservation()));

        mockMvc.perform(get("/api/reservations"))
                .andExpect(status().isOk());

        verify(reservationService, times(1)).getAll();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetById() throws Exception {
        Long id = 1L;
        when(reservationService.getById(id)).thenReturn(new Reservation());

        mockMvc.perform(get("/api/reservations/" + id))
                .andExpect(status().isOk());

        verify(reservationService, times(1)).getById(id);
    }

    @Test
    @WithMockUser
    void testCreateReservation() throws Exception {
        when(reservationService.createReservation(any(ReservationDto.class))).thenReturn(ResponseEntity.ok("Success"));

        mockMvc.perform(post("/api/reservations/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testConfirmReservation() throws Exception {
        Long id = 1L;
        doNothing().when(reservationService).confirmReservation(id);

        mockMvc.perform(post("/api/reservations/" + id + "/confirm"))
                .andExpect(status().isOk());

        verify(reservationService, times(1)).confirmReservation(id);
    }
}
