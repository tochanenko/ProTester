package ua.project.protester.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ua.project.protester.model.User;
import ua.project.protester.service.UserService;

import java.util.Optional;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    private UserService userService;

    @Autowired
    public JwtUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> user = userService.findUserByEmail(email);

        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User with email: " + email + " not found");
        }
        return new UserPrincipal(user.get());
    }
}
