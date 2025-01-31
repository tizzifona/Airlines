package projects.f5.airlines.registration;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import projects.f5.airlines.exception.UserAlreadyExistsException;
import projects.f5.airlines.role.Role;
import projects.f5.airlines.role.UserRoleRepository;
import projects.f5.airlines.user.User;
import projects.f5.airlines.user.UserDto;
import projects.f5.airlines.user.UserRepository;

@ExtendWith(MockitoExtension.class)
public class UserRegistrationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRoleRepository userRoleRepository;

    @InjectMocks
    private UserRegistrationService userRegistrationService;

    private UserDto userDto;

    @BeforeEach
    void setUp() {
        userDto = new UserDto(
                null,
                "testUser",
                "password123",
                "test@example.com",
                null,
                Set.of(Role.ROLE_USER),
                null);
    }

    @Test
    void testRegisterUser_Success() {
        when(userRepository.findByUsername(userDto.username())).thenReturn(Optional.empty());
        when(userRepository.findByEmail(userDto.email())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(userDto.password())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserDto registeredUser = userRegistrationService.registerUser(userDto);

        assertNotNull(registeredUser);
        assertEquals(userDto.username(), registeredUser.username());
        assertEquals("encodedPassword", registeredUser.password());
        assertEquals(userDto.email(), registeredUser.email());
        assertEquals("default_profile.png", registeredUser.profileImage());
    }

    @Test
    void testRegisterUser_UsernameAlreadyExists() {
        when(userRepository.findByUsername(userDto.username())).thenReturn(Optional.of(new User()));

        assertThrows(UserAlreadyExistsException.class, () -> userRegistrationService.registerUser(userDto));
    }

    @Test
    void testRegisterUser_EmailAlreadyExists() {
        when(userRepository.findByEmail(userDto.email())).thenReturn(Optional.of(new User()));

        assertThrows(UserAlreadyExistsException.class, () -> userRegistrationService.registerUser(userDto));
    }
}