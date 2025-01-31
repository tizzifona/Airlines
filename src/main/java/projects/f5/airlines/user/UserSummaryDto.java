package projects.f5.airlines.user;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record UserSummaryDto(
        Long id,
        String username,
        String password,
        String profileImage,
        Set<String> roles) {
}
