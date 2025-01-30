package projects.f5.airlines.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import projects.f5.airlines.registration.UserRegistrationService;
import projects.f5.airlines.role.Role;
import projects.f5.airlines.role.UserRole;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserRegistrationService userRegistrationService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User user;
    private UserDto userDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        Set<UserRole> roles = new HashSet<>();
        roles.add(new UserRole(Role.ROLE_USER));

        user = new User(1L, "testUser", "password", "test@test.com", "profileImage.jpg", roles, new HashSet<>());
        userDto = new UserDto(1L, "testUser", "password", "test@test.com", "profileImage.jpg", Set.of(Role.ROLE_USER),
                new HashSet<>());
    }

    @Test
    void testDeleteUser() {
        when(userRepository.existsById(1L)).thenReturn(true);

        userService.deleteUser(1L);

        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void testFindById() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Optional<User> result = userService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(user, result.get());
    }

    @Test
    void testGetAllUsers() {
        when(userRepository.findAll()).thenReturn(List.of(user));

        List<UserSummaryDto> result = userService.getAllUsers();

        assertEquals(1, result.size());
        assertEquals(user.getUsername(), result.get(0).username());
    }

    @Test
    void testGetUserById() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Optional<UserDetailDto> result = userService.getUserById(1L, user);

        assertTrue(result.isPresent());
        assertEquals(user.getUsername(), result.get().username());
    }

    @Test
    void testGetUserByUsername() {
        when(userRepository.findByUsernameWithRoles("testUser")).thenReturn(Optional.of(user));

        Optional<UserDto> result = userService.getUserByUsername("testUser");

        assertTrue(result.isPresent());
        assertEquals(user.getUsername(), result.get().username());
    }

    @Test
    void testRegisterUser() {
        when(userRegistrationService.registerUser(userDto)).thenReturn(userDto);

        UserDto result = userService.registerUser(userDto);

        assertEquals(userDto.username(), result.username());
    }

    @Test
    void testUpdateUser() {

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        userDto = new UserDto(1L, "newUsername", null, null, null, Set.of(Role.ROLE_USER), new HashSet<>());

        UserDto result = userService.updateUser(1L, userDto);

        assertEquals("newUsername", result.username());
        assertEquals("password", result.password());
        assertEquals("profileImage.jpg", result.profileImage());
    }

    @Test
    void testUploadProfileImage() throws IOException {
        MultipartFile file = mock(MultipartFile.class);
        when(file.getOriginalFilename()).thenReturn("newProfileImage.jpg");
        when(file.getBytes()).thenReturn(new byte[0]);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        String result = userService.uploadProfileImage(1L, file, user);

        Path expectedPath = Paths.get("uploads/newProfileImage.jpg");
        assertEquals(expectedPath.toString(), result);
        verify(userRepository, times(1)).save(user);
    }

}
