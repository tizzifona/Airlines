package projects.f5.airlines.user;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import projects.f5.airlines.role.Role;
import projects.f5.airlines.role.UserRole;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private static final String UPLOAD_DIR = "uploads/";

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<UserDto> getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(this::convertToDto);
    }

    public UserDto registerUser(UserDto userDto) {
        if (userRepository.findByUsername(userDto.username()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }
        User user = new User(
                null,
                userDto.username(),
                passwordEncoder.encode(userDto.password()),
                userDto.profileImage() != null ? userDto.profileImage() : "default_profile.png",
                convertRolesToUserRoles(userDto.roles()),
                userDto.reservations());
        user = userRepository.save(user);
        return convertToDto(user);
    }

    public UserDto updateUser(Long id, UserDto userDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setUsername(userDto.username());
        user.setProfileImage(userDto.profileImage() != null ? userDto.profileImage() : "default_profile.png");
        user.setPassword(passwordEncoder.encode(userDto.password()));
        user.setRoles(convertRolesToUserRoles(userDto.roles()));
        return convertToDto(userRepository.save(user));
    }

    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found");
        }
        userRepository.deleteById(id);
    }

    public String uploadProfileImage(Long userId, MultipartFile file) {
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
                user.getProfileImage(),
                roles,
                user.getReservations());
    }

    private Set<UserRole> convertRolesToUserRoles(Set<Role> roles) {
        return roles.stream()
                .map(role -> new UserRole())
                .collect(Collectors.toSet());
    }

    public Optional<User> findById(Long userId) {
        return userRepository.findById(userId);
    }

}
