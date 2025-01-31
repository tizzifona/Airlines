package projects.f5.airlines.security;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api")
public class AuthController {

    @PostMapping(path = "/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequest loginRequest) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        Map<String, String> json = new HashMap<>();
        json.put("message", "Logged");
        json.put("username", auth.getName());

        StringBuilder roles = new StringBuilder();
        auth.getAuthorities().forEach(authority -> roles.append(authority.getAuthority()).append(", "));

        json.put("roles", roles.length() > 0 ? roles.substring(0, roles.length() - 2) : "");

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(json);
    }
}
