package projects.f5.airlines.security;

import java.util.ArrayList;
import java.util.Collection;

import org.hibernate.Hibernate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.transaction.Transactional;
import projects.f5.airlines.role.UserRole;
import projects.f5.airlines.user.User;

public class SecurityUser implements UserDetails {

    private User user;

    public SecurityUser(User user) {
        this.user = user;
        Hibernate.initialize(user.getRoles());
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    @Transactional
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> authorities = new ArrayList<>();

        if (user.getRoles() != null) {
            for (UserRole userRole : user.getRoles()) {
                System.out.println("User role: " + userRole.getName());
                SimpleGrantedAuthority authority = new SimpleGrantedAuthority(userRole.getName());
                authorities.add(authority);
            }
        }

        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    public User getUser() {
        return user;
    }

    public Long getId() {
        return user.getId();
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
