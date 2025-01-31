package projects.f5.airlines.role;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import projects.f5.airlines.user.User;

public class UserRoleTest {
    @Test
    void testGetId() {
        UserRole userRole = new UserRole();
        assertThat(userRole.getId()).isNull();
    }

    @Test
    void testGetName() {
        UserRole userRole = new UserRole(Role.ROLE_ADMIN);
        assertThat(userRole.getName()).isEqualTo("ROLE_ADMIN");
    }

    @Test
    void testGetRole() {
        UserRole userRole = new UserRole(Role.ROLE_USER);
        assertThat(userRole.getRole()).isEqualTo(Role.ROLE_USER);
    }

    @Test
    void testGetUser() {
        User user = mock(User.class);
        UserRole userRole = new UserRole();
        userRole.setUser(user);
        assertThat(userRole.getUser()).isEqualTo(user);
    }

    @Test
    void testSetUser() {
        User user = mock(User.class);
        UserRole userRole = new UserRole();
        userRole.setUser(user);
        assertThat(userRole.getUser()).isSameAs(user);
    }
}