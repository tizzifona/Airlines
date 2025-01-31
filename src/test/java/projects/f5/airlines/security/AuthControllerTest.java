package projects.f5.airlines.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AuthControllerTest {

    private MockMvc mockMvc;
    private AuthController authController;

    @BeforeEach
    void setUp() {
        authController = new AuthController();
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }

    @Test
    void testLoginWhenNotAuthenticated() throws Exception {
        SecurityContext context = Mockito.mock(SecurityContext.class);
        SecurityContextHolder.setContext(context);
        when(context.getAuthentication()).thenReturn(null);

        mockMvc.perform(post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string(""));
    }

    @Test
    void testLoginWhenAuthenticated() throws Exception {
        User user = new User("testUser", "password", Collections.singletonList(() -> "ROLE_USER"));
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null,
                user.getAuthorities());

        SecurityContext context = Mockito.mock(SecurityContext.class);
        SecurityContextHolder.setContext(context);
        when(context.getAuthentication()).thenReturn(authentication);

        mockMvc.perform(post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.message").value("Logged"))
                .andExpect(jsonPath("$.username").value("testUser"))
                .andExpect(jsonPath("$.roles").value("ROLE_USER"));
    }

    @Test
    void testLoginWhenAuthenticatedWithMultipleRoles() throws Exception {

        User user = new User("testUser", "password", Collections.singletonList(() -> "ROLE_USER"));
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null,
                user.getAuthorities());

        SecurityContext context = Mockito.mock(SecurityContext.class);
        SecurityContextHolder.setContext(context);
        when(context.getAuthentication()).thenReturn(authentication);

        mockMvc.perform(post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.message").value("Logged"))
                .andExpect(jsonPath("$.username").value("testUser"))
                .andExpect(jsonPath("$.roles").value("ROLE_USER"));
    }
}
