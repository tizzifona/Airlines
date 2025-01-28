package projects.f5.airlines.user;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;

import projects.f5.airlines.reservation.Reservation;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record UserDetailDto(
        Long id,
        String username,
        String password,
        String profileImage,
        Set<String> roles,
        Set<Reservation> reservations) {
}
