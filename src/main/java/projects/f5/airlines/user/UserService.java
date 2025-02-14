package projects.f5.airlines.user;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import projects.f5.airlines.registration.UserRegistrationService;
import projects.f5.airlines.role.Role;
import projects.f5.airlines.role.UserRole;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserRegistrationService userRegistrationService;
    private final PasswordEncoder passwordEncoder;
    private static final String UPLOAD_DIR = "uploads/";

    public UserService(UserRepository userRepository, UserRegistrationService userRegistrationService,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userRegistrationService = userRegistrationService;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public List<UserSummaryDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToSummaryDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public Optional<UserDetailDto> getUserById(Long id, User currentUser) {
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (currentUser.getRoles().stream().anyMatch(role -> role.getRole().equals(Role.ROLE_USER))
                    && !currentUser.getId().equals(id)) {
                throw new RuntimeException("Access denied");
            }
            return Optional.of(convertToDetailedDto(user));
        }
        return Optional.empty();
    }

    private UserSummaryDto convertToSummaryDto(User user) {
        Set<String> roles = user.getRoles().stream()
                .map(userRole -> userRole.getRole().name())
                .collect(Collectors.toSet());
        return new UserSummaryDto(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.getProfileImage(),
                roles);
    }

    private UserDetailDto convertToDetailedDto(User user) {
        Set<String> roles = user.getRoles().stream()
                .map(userRole -> userRole.getRole().name())
                .collect(Collectors.toSet());
        return new UserDetailDto(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.getProfileImage(),
                roles,
                user.getReservations());
    }

    @Transactional
    public Optional<UserDto> getUserByUsername(String username) {
        return userRepository.findByUsernameWithRoles(username)
                .map(this::convertToDto);
    }

    public UserDto updateUser(Long id, UserDto userDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (userDto.username() != null && !userDto.username().isBlank()) {
            user.setUsername(userDto.username());
        }

        if (userDto.profileImage() != null) {
            user.setProfileImage(userDto.profileImage());
        }

        if (userDto.password() != null && !userDto.password().isBlank()) {
            user.setPassword(passwordEncoder.encode(userDto.password()));
        }

        if (userDto.roles() != null) {
            user.setRoles(convertRolesToUserRoles(userDto.roles()));
        }

        return convertToDto(userRepository.save(user));
    }

    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found");
        }
        userRepository.deleteById(id);
    }

    public String uploadProfileImage(Long userId, MultipartFile file, User currentUser) {
        if (currentUser.getRoles().stream().anyMatch(role -> role.getRole().equals(Role.ROLE_USER))
                && !currentUser.getId().equals(userId)) {
            throw new RuntimeException("Access denied");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        try {
            byte[] bytes = file.getBytes();
            Path path = Paths.get(UPLOAD_DIR + file.getOriginalFilename());
            Files.write(path, bytes);
            user.setProfileImage(path.toString());
            userRepository.save(user);
            return path.toString();
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload image", e);
        }
    }

    private UserDto convertToDto(User user) {
        Set<Role> roles = user.getRoles().stream()
                .map(UserRole::getRole)
                .collect(Collectors.toSet());
        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.getEmail(),
                user.getProfileImage(),
                roles,
                user.getReservations());
    }

    private Set<UserRole> convertRolesToUserRoles(Set<Role> roles) {
        return roles.stream()
                .map(UserRole::new)
                .collect(Collectors.toSet());
    }

    public Optional<User> findById(Long userId) {
        return userRepository.findById(userId);
    }

    public UserDto registerUser(UserDto userDto) {
        return userRegistrationService.registerUser(userDto);
    }
}
