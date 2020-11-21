package ua.project.protester.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ua.project.protester.model.UserDto;
import ua.project.protester.response.UserLoginResponse;
import ua.project.protester.security.UserPrincipal;
import ua.project.protester.utils.JwtUtils;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthorizationService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    public UserLoginResponse authenticate(UserDto userDto) {
        String bearer = "BEARER ";
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userDto.getEmail(), userDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserPrincipal userDetails = (UserPrincipal) authentication.getPrincipal();
        String role = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.joining());
        return new UserLoginResponse(bearer + jwt, role);
    }
}
