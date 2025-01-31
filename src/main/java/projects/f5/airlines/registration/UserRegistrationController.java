package projects.f5.airlines.registration;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import projects.f5.airlines.exception.UserAlreadyExistsException;
import projects.f5.airlines.user.UserDto;

@RestController
@RequestMapping("/api/register")
public class UserRegistrationController {
    private final UserRegistrationService userRegistrationService;

    public UserRegistrationController(UserRegistrationService userRegistrationService) {
        this.userRegistrationService = userRegistrationService;
    }

    @PostMapping
    public ResponseEntity<UserDto> registerUser(@RequestBody UserDto userDto) {
        try {
            UserDto createdUser = userRegistrationService.registerUser(userDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
        } catch (UserAlreadyExistsException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
}
