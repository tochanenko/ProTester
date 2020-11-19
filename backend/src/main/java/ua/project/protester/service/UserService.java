package ua.project.protester.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.project.protester.exception.MailSendException;
import ua.project.protester.model.User;
import ua.project.protester.model.UserDto;
import ua.project.protester.repository.UserRepository;
import ua.project.protester.request.UserCreationRequestDto;
import ua.project.protester.request.UserModificationDto;
import ua.project.protester.response.UserLoginResponse;
import ua.project.protester.response.UserResponse;
import ua.project.protester.security.UserPrincipal;
import ua.project.protester.utils.JwtUtils;
import ua.project.protester.utils.UserMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final RoleService roleService;
    private final MailService mailService;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    public UserService(UserMapper userMapper, UserRepository userRepository, RoleService roleService, MailService mailService, PasswordEncoder passwordEncoder) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.mailService = mailService;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public int createUser(UserCreationRequestDto userRequest) throws MailSendException {
        User user = userMapper.toUserFromUserRequest(userRequest);
        user.setRole(roleService.findRoleByName(user.getRole().getName()));
        user.setActive(true);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        mailService.sendRegistrationCredentials(userRequest);
        return userRepository.save(user);
    }

    @Transactional
    public User findUserById(Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            Long roleId = user.getRole().getId();
            user.setRole(roleService.findRoleById(roleId));
            return user;
        }
        return null;
    }

    @Transactional
    public User findUserByEmail(String email) {
        User user = userRepository.findUserByEmail(email).orElse(null);
        if (user != null) {
            Long roleId = user.getRole().getId();
            user.setRole(roleService.findRoleById(roleId));
            return user;
        }
        return null;
    }

    @Transactional
    public User findUserByUsername(String username) {
        User user = userRepository.findUserByUsername(username).orElse(null);
        if (user != null) {
            Long roleId = user.getRole().getId();
            user.setRole(roleService.findRoleById(roleId));
            return user;
        }
        return null;
    }

    @Transactional
    public void updateUser(UserModificationDto userDto) {
        User user = userMapper.toUserFromUserModificationDto(userDto);
        user.setRole(roleService.findRoleByName(user.getRole().getName()));
        user.getRole().getUsers().add(user);
        userRepository.update(user);
    }

    @Transactional
    public void deleteUser(User user) {
       user.getRole().getUsers().remove(user);
       userRepository.delete(user);
    }

    @Transactional
    public List<User> findAll() {
        return userRepository.findAll();
    }

    public UserResponse getUser(Long id) {
        return  userMapper.toUserRest(findUserById(id));
    }

    public List<UserResponse> getAllUsers() {
        return findAll().stream().map(userMapper::toUserRest).collect(Collectors.toList());
    }

    public UserLoginResponse authenticate(UserDto userDto) {
        String bearer = "BEARER ";
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userDto.getEmail(), userDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserPrincipal userDetails = (UserPrincipal) authentication.getPrincipal();
        String role = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority()).collect(Collectors.joining());
       return new UserLoginResponse(bearer + jwt, role);
    }
}
