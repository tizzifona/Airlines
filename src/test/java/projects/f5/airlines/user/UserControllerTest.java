package projects.f5.airlines.user;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import projects.f5.airlines.security.SecurityUser;

import java.util.*;

class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private User testUser;
    private SecurityUser securityUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        testUser = new User(1L, "username", "password", "email@example.com", "image.jpg", new HashSet<>(),
                new HashSet<>());
        securityUser = new SecurityUser(testUser);
    }

    @Test
    void testDeleteUser() {
        Long userId = 1L;
        doNothing().when(userService).deleteUser(userId);

        ResponseEntity<Void> response = userController.deleteUser(userId);

        verify(userService, times(1)).deleteUser(userId);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void testGetAllUsers() {
        List<UserSummaryDto> userSummaryList = Collections
                .singletonList(new UserSummaryDto(1L, "username", "password", "image.jpg", new HashSet<>()));
        when(userService.getAllUsers()).thenReturn(userSummaryList);

        ResponseEntity<List<UserSummaryDto>> response = userController.getAllUsers();

        verify(userService, times(1)).getAllUsers();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userSummaryList, response.getBody());
    }

    @Test
    void testGetUserById_Success() {
        Long userId = 1L;
        when(userService.findById(1L)).thenReturn(Optional.of(testUser));
        UserDetailDto userDetailDto = new UserDetailDto(1L, "username", "password", "image.jpg", new HashSet<>(),
                new HashSet<>());
        when(userService.getUserById(userId, testUser)).thenReturn(Optional.of(userDetailDto));

        ResponseEntity<UserDetailDto> response = userController.getUserById(userId, securityUser);

        verify(userService, times(1)).findById(1L);
        verify(userService, times(1)).getUserById(userId, testUser);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userDetailDto, response.getBody());
    }

    @Test
    void testGetUserById_Unauthorized() {
        Long userId = 1L;
        when(userService.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<UserDetailDto> response = userController.getUserById(userId, securityUser);

        verify(userService, times(1)).findById(1L);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void testGetUserById_NotFound() {
        Long userId = 1L;
        when(userService.findById(1L)).thenReturn(Optional.of(testUser));
        when(userService.getUserById(userId, testUser)).thenReturn(Optional.empty());

        ResponseEntity<UserDetailDto> response = userController.getUserById(userId, securityUser);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testUpdateUser() {
        Long userId = 1L;
        UserDto userDto = new UserDto(1L, "updatedUsername", "updatedPassword", "updatedEmail", "updatedImage.jpg",
                new HashSet<>(), new HashSet<>());
        when(userService.updateUser(userId, userDto)).thenReturn(userDto);

        ResponseEntity<UserDto> response = userController.updateUser(userId, userDto);

        verify(userService, times(1)).updateUser(userId, userDto);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userDto, response.getBody());
    }

    @Test
    void testUploadProfileImage() {
        Long userId = 1L;
        MultipartFile file = mock(MultipartFile.class);
        when(file.getOriginalFilename()).thenReturn("image.jpg");
        when(userService.findById(1L)).thenReturn(Optional.of(testUser));
        when(userService.uploadProfileImage(userId, file, testUser)).thenReturn("path/to/image.jpg");

        ResponseEntity<Map<String, String>> response = userController.uploadProfileImage(userId, file, securityUser);

        verify(userService, times(1)).uploadProfileImage(userId, file, testUser);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        Map<String, String> responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals("path/to/image.jpg", responseBody.get("imageUrl"));
        assertEquals("Image uploaded successfully", responseBody.get("message"));
    }

    @Test
    void testUploadProfileImage_Unauthorized() {
        Long userId = 1L;
        MultipartFile file = mock(MultipartFile.class);
        when(file.getOriginalFilename()).thenReturn("image.jpg");
        when(userService.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<Map<String, String>> response = userController.uploadProfileImage(userId, file, securityUser);

        verify(userService, times(1)).findById(1L);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

}
