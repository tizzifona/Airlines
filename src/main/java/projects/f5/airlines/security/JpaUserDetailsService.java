package projects.f5.airlines.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import projects.f5.airlines.user.UserRepository;

@Service
public class JpaUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;

    public JpaUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsernameWithRoles(username)
                .map(SecurityUser::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }

}
