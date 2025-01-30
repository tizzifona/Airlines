package projects.f5.airlines.registration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import projects.f5.airlines.exception.UserAlreadyExistsException;
import projects.f5.airlines.user.UserDto;
import projects.f5.airlines.role.Role;

import java.util.Set;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class UserRegistrationControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserRegistrationService userRegistrationService;

    @InjectMocks
    private UserRegistrationController userRegistrationController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userRegistrationController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testRegisterUser_Success() throws Exception {

        UserDto userDto = new UserDto(
                1L,
                "username",
                "password",
                "user@example.com",
                "profileImage.png",
                Set.of(Role.ROLE_USER),
                Set.of());

        when(userRegistrationService.registerUser(any(UserDto.class))).thenReturn(userDto);

        mockMvc.perform(post("/api/register")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("username"))
                .andExpect(jsonPath("$.email").value("user@example.com"))
                .andExpect(jsonPath("$.roles[0]").value("ROLE_USER"));

        verify(userRegistrationService, times(1)).registerUser(any(UserDto.class));
    }

    @Test
    void testRegisterUser_UserAlreadyExists() throws Exception {

        UserDto userDto = new UserDto(
                null,
                "existingUser",
                "password",
                "existing@example.com",
                "profileImage.png",
                Set.of(Role.ROLE_USER),
                Set.of());

        when(userRegistrationService.registerUser(any(UserDto.class)))
                .thenThrow(new UserAlreadyExistsException("User already exists"));

        mockMvc.perform(post("/api/register")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isBadRequest());

        verify(userRegistrationService, times(1)).registerUser(any(UserDto.class));
    }
}
