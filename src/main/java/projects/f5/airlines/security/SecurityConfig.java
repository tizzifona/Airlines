package projects.f5.airlines.security;

import static org.springframework.security.config.Customizer.withDefaults;
import static org.springframework.http.HttpMethod.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${api-endpoint}")
    String endpoint;

    private final JpaUserDetailsService jpaUserDetailsService;

    public SecurityConfig(JpaUserDetailsService userDetailsService) {
        this.jpaUserDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(withDefaults())
                .csrf(csrf -> csrf.disable())
                .formLogin(form -> form.disable())
                .logout(out -> out
                        .logoutUrl(endpoint + "/logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID"))
                .authorizeHttpRequests(auth -> auth
                        // Authentication
                        .requestMatchers(POST, endpoint + "/login").permitAll()
                        .requestMatchers(POST, endpoint + "/register").permitAll()

                        // Airports
                        .requestMatchers(GET, endpoint + "/airports").hasRole("ADMIN")
                        .requestMatchers(GET, endpoint + "/airports/{id}").hasRole("ADMIN")
                        .requestMatchers(POST, endpoint + "/airports").hasRole("ADMIN")
                        .requestMatchers(PUT, endpoint + "/airports/{id}").hasRole("ADMIN")
                        .requestMatchers(DELETE, endpoint + "/airports/{id}").hasRole("ADMIN")

                        // Flights
                        .requestMatchers(GET, endpoint + "/flights/search").hasAnyRole("ADMIN", "USER")
                        .requestMatchers(GET, endpoint + "/flights").hasRole("ADMIN")
                        .requestMatchers(GET, endpoint + "/flights/{id}").hasRole("ADMIN")
                        .requestMatchers(POST, endpoint + "/flights").hasRole("ADMIN")
                        .requestMatchers(PUT, endpoint + "/flights/{id}/seats").hasRole("ADMIN")
                        .requestMatchers(PUT, endpoint + "/flights/{id}/availability").hasRole("ADMIN")
                        .requestMatchers(DELETE, endpoint + "/flights/{id}").hasRole("ADMIN")

                        // Users
                        .requestMatchers(GET, endpoint + "/users").hasRole("ADMIN")
                        .requestMatchers(GET, endpoint + "/users/{id}").hasAnyRole("ADMIN", "USER")
                        .requestMatchers(PUT, endpoint + "/users/{id}").hasRole("ADMIN")
                        .requestMatchers(DELETE, endpoint + "/users/{id}").hasRole("ADMIN")
                        .requestMatchers(GET, endpoint + "/reservations/my-reservations").hasRole("USER")
                        .requestMatchers(POST, endpoint + "/users/{id}/upload").hasRole("USER")

                        // Reservations
                        .requestMatchers(GET, endpoint + "/reservations").hasRole("ADMIN")
                        .requestMatchers(GET, endpoint + "/reservations/{id}").hasRole("ADMIN")
                        .requestMatchers(POST, endpoint + "/reservations/create").hasRole("USER")
                        .requestMatchers(POST, endpoint + "/reservations/{id}/confirm").hasRole("USER")
                        .requestMatchers(DELETE, endpoint + "/reservations/{id}").hasRole("ADMIN")

                        .anyRequest().authenticated())

                .userDetailsService(jpaUserDetailsService)
                .httpBasic(withDefaults())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED));

        http.headers(header -> header.frameOptions(frame -> frame.sameOrigin()));

        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
