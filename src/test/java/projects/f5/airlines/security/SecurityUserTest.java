package projects.f5.airlines.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import projects.f5.airlines.role.UserRole;
import projects.f5.airlines.user.User;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SecurityUserTest {

    @Mock
    private User user;

    @Mock
    private UserRole userRole;

    @InjectMocks
    private SecurityUser securityUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        when(user.getUsername()).thenReturn("testuser");
        when(user.getPassword()).thenReturn("password");
        when(user.getId()).thenReturn(1L);

        Set<UserRole> roles = new HashSet<>();
        roles.add(userRole);
        when(user.getRoles()).thenReturn(roles);

        when(userRole.getName()).thenReturn("ROLE_USER");
    }

    @Test
    void testGetAuthorities() {

        Collection<? extends GrantedAuthority> authorities = securityUser.getAuthorities();
        assertNotNull(authorities);
        assertEquals(1, authorities.size());
        assertTrue(authorities.contains(new SimpleGrantedAuthority("ROLE_USER")));
    }

    @Test
    void testGetId() {
        Long id = securityUser.getId();
        assertEquals(1L, id);
    }

    @Test
    void testGetPassword() {
        String password = securityUser.getPassword();
        assertEquals("password", password);
    }

    @Test
    void testGetUser() {
        User actualUser = securityUser.getUser();
        assertNotNull(actualUser);
        assertEquals(user, actualUser);
    }

    @Test
    void testGetUsername() {
        String username = securityUser.getUsername();
        assertEquals("testuser", username);
    }

    @Test
    void testIsAccountNonExpired() {
        boolean isAccountNonExpired = securityUser.isAccountNonExpired();
        assertTrue(isAccountNonExpired);
    }

    @Test
    void testIsAccountNonLocked() {
        boolean isAccountNonLocked = securityUser.isAccountNonLocked();
        assertTrue(isAccountNonLocked);
    }

    @Test
    void testIsCredentialsNonExpired() {
        boolean isCredentialsNonExpired = securityUser.isCredentialsNonExpired();
        assertTrue(isCredentialsNonExpired);
    }

    @Test
    void testIsEnabled() {
        boolean isEnabled = securityUser.isEnabled();
        assertTrue(isEnabled);
    }
}
