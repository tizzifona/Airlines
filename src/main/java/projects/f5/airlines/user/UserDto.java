package projects.f5.airlines.user;

import projects.f5.airlines.reservation.Reservation;
import projects.f5.airlines.role.Role;
import java.util.Set;

public record UserDto(
        Long id,
        String username,
        String password,
        String profileImage,
        Set<Role> roles,
        Set<Reservation> reservations) {
}
