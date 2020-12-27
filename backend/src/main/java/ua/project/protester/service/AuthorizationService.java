package ua.project.protester.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ua.project.protester.exception.RoleNotFoundException;
import ua.project.protester.exception.UserNotExistException;
import ua.project.protester.model.User;
import ua.project.protester.model.UserDto;
import ua.project.protester.repository.UserRepository;
import ua.project.protester.response.UserLoginResponse;
import ua.project.protester.utils.JwtUtils;

@Service
@RequiredArgsConstructor
public class AuthorizationService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;

    public UserLoginResponse authenticate(UserDto userDto) {
        String bearer = "Bearer ";
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userDto.getEmail(), userDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String role = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElseThrow(() -> new RoleNotFoundException("No role was not found!"));
        User user = userRepository.findUserByEmail(userDetails.getUsername())
                .orElseThrow(() -> new UserNotExistException("User with email" + userDetails.getUsername() + "was`nt found!"));
        return new UserLoginResponse(user.getId(), user.getUsername(), user.getEmail(), bearer + jwt, role);
    }
}
