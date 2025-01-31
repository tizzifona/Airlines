package projects.f5.airlines.security;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import projects.f5.airlines.user.User;
import projects.f5.airlines.user.UserRepository;

public class JpaUserDetailsServiceTest {

    @Mock
    private UserRepository mockUserRepository;
    private JpaUserDetailsService jpaUserDetailsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        jpaUserDetailsService = new JpaUserDetailsService(mockUserRepository);
    }

    @Test
    void testGetUserRepository() {
        assertNotNull(jpaUserDetailsService.getUserRepository());
    }

    @Test
    void testLoadUserByUsername_UserFound() {
        User mockUser = mock(User.class);
        when(mockUserRepository.findByUsernameWithRoles("testUser"))
                .thenReturn(Optional.of(mockUser));

        UserDetails userDetails = jpaUserDetailsService.loadUserByUsername("testUser");

        assertNotNull(userDetails);
        assertTrue(userDetails instanceof SecurityUser);

        verify(mockUserRepository, times(1)).findByUsernameWithRoles("testUser");
    }

    @Test
    void testLoadUserByUsername_UserNotFound() {

        when(mockUserRepository.findByUsernameWithRoles("unknownUser"))
                .thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> {
            jpaUserDetailsService.loadUserByUsername("unknownUser");
        });

        verify(mockUserRepository, times(1)).findByUsernameWithRoles("unknownUser");
    }
}
