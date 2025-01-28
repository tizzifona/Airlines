package projects.f5.airlines.registration;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import projects.f5.airlines.exception.UserAlreadyExistsException;
import projects.f5.airlines.role.Role;
import projects.f5.airlines.role.UserRole;
import projects.f5.airlines.role.UserRoleRepository;
import projects.f5.airlines.user.User;
import projects.f5.airlines.user.UserDto;
import projects.f5.airlines.user.UserRepository;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserRegistrationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRoleRepository userRoleRepository;

    public UserRegistrationService(UserRepository userRepository, PasswordEncoder passwordEncoder,
            UserRoleRepository userRoleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userRoleRepository = userRoleRepository;
    }

    public UserDto registerUser(UserDto userDto) {
        if (userRepository.findByUsername(userDto.username()).isPresent()) {
            throw new UserAlreadyExistsException("Username already exists: " + userDto.username());
        }

        if (userRepository.findByEmail(userDto.email()).isPresent()) {
            throw new UserAlreadyExistsException("Email already exists: " + userDto.email());
        }

        Set<Role> roles = Optional.ofNullable(userDto.roles())
                .filter(r -> !r.isEmpty())
                .orElse(Set.of(Role.ROLE_USER));

        Set<UserRole> userRoles = convertRolesToUserRoles(roles);

        User user = new User(
                null,
                userDto.username(),
                passwordEncoder.encode(userDto.password()),
                userDto.email(),
                userDto.profileImage() != null ? userDto.profileImage() : "default_profile.png",
                userRoles,
                userDto.reservations());

        user = userRepository.save(user);

        for (UserRole role : userRoles) {
            role.setUser(user);
            userRoleRepository.save(role);
        }

        return convertToDto(user);
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
}
