package ua.project.protester.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.project.protester.exception.RoleNotFoundException;
import ua.project.protester.exception.UserNotExistException;
import ua.project.protester.exception.MailSendException;
import ua.project.protester.exception.UsernameDuplicateException;
import ua.project.protester.exception.UnauthorizedUserException;
import ua.project.protester.exception.EmailDuplicateException;
import ua.project.protester.exception.FaultPermissionException;
import ua.project.protester.model.User;
import ua.project.protester.repository.UserRepository;
import ua.project.protester.request.UserCreationRequestDto;
import ua.project.protester.request.UserModificationDto;
import ua.project.protester.response.UserResponse;
import ua.project.protester.utils.UserMapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final RoleService roleService;
    private final MailService mailService;
    private final PasswordEncoder passwordEncoder;

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);


    @Transactional
    public void createUser(UserCreationRequestDto userRequest) throws MailSendException {
        User user = userMapper.toUserFromUserRequest(userRequest);
        user.setRole(roleService.findRoleByName(user.getRole().getName())
                .orElseThrow(() -> new RoleNotFoundException("Role was`nt found!")));
        user.setActive(true);
        logger.info("Creating user {}", user);
        mailService.sendRegistrationCredentials(user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Transactional
    public void updateUser(UserModificationDto userDto) {
        User user = userMapper.toUserFromUserModificationDto(userDto);
        user.setRole(roleService.findRoleByName(user.getRole().getName())
                .orElseThrow(() -> new RoleNotFoundException("Role was`nt found!")));
        logger.info("Updating user {}", user);
        userRepository.update(user);
    }

    @Transactional
    public Optional<User> findUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotExistException("User with id" + id + "does`nt exist"));
        Long roleId = user.getRole().getId();
        user.setRole(roleService.findRoleById(roleId)
                .orElseThrow(() -> new RoleNotFoundException("Role was`nt found!")));
            return Optional.of(user);
    }

    @Transactional
    public Optional<User> findUserByEmail(String email) {
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new UserNotExistException("User with email" + email + "does`nt exist"));
        Long roleId = user.getRole().getId();
        user.setRole(roleService.findRoleById(roleId)
                    .orElseThrow(() -> new RoleNotFoundException("Role was`nt found!")));
        return Optional.of(user);
    }

    @Transactional
    public Optional<User> findUserByUsername(String username) {
        User user = userRepository.findUserByUsername(username).orElseThrow(() -> new UserNotExistException("User with username" + username + "does`nt exist"));
        Long roleId = user.getRole().getId();
        user.setRole(roleService.findRoleById(roleId)
                    .orElseThrow(() -> new RoleNotFoundException("Role was`nt found!")));
            return Optional.of(user);
    }

    public UserResponse getUser(Long id) {
        return userMapper
                .toUserRest(findUserById(id)
                .orElseThrow(() -> new UserNotExistException("User with id " + id + " was`nt found")));
    }

    public UserResponse findUsersByUsername(String username) {
        return userMapper
                .toUserRest(findUserByUsername(username)
                        .orElseThrow(() -> new UserNotExistException("Username " + username + " was not found")));
    }

    public UserResponse findUsersByEmail(String email) {

        return userMapper.toUserRest(findUserByEmail(email).orElseThrow());
    }

    @Transactional
    public List<User> findAll() {
        List<User> users = userRepository.findAll();
        users.forEach(user -> user.setRole(roleService.findRoleById(user.getRole().getId())
                .orElseThrow(() -> new RoleNotFoundException("Role was`nt found!"))));
        return users;
    }

    @Transactional
    public List<UserResponse> findUsersByRoleName(String roleName) {
        List<User> users = userRepository.findUserByRolename(roleName);
        users.forEach(user -> user.setRole(roleService.findRoleById(user.getRole().getId())
                .orElseThrow(() -> new RoleNotFoundException("Role was`nt found!"))));
        return users.stream().map(userMapper::toUserRest).collect(Collectors.toList());
    }

    @Transactional
    public List<UserResponse> findUsersByName(String name) {
        List<User> users = userRepository.findUsersByName(name);
        users.forEach(user -> user.setRole(roleService.findRoleById(user.getRole().getId())
                .orElseThrow(() -> new RoleNotFoundException("Role was`nt found!"))));
        return users.stream().map(userMapper::toUserRest).collect(Collectors.toList());
    }

    @Transactional
    public List<UserResponse> findUsersBySurname(String surname) {
        List<User> users = userRepository.findUsersBySurname(surname);
        users.forEach(user -> user.setRole(roleService.findRoleById(user.getRole().getId())
          .orElseThrow(() -> new RoleNotFoundException("Role was`nt found!"))));
        return users.stream()
                .map(userMapper::toUserRest)
                .collect(Collectors.toList());
    }

    public List<UserResponse> getAllUsers() {
        return findAll()
                .stream()
                .map(userMapper::toUserRest)
                .collect(Collectors.toList());
    }

    private Optional<User> currentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            return findUserByEmail(authentication.getName());
        }
        return Optional.empty();
    }

    public UserResponse currentUserDetails() {
        return userMapper
                .toUserRest(currentUser()
                .orElseThrow(() -> new UnauthorizedUserException("You are not authorized!")));
    }

    public void modifyUser(UserModificationDto userDto) {
        User user = currentUser()
                .orElseThrow(() -> new UnauthorizedUserException("You are not authorized!"));
        User userFromDB = findUserById(userDto.getId())
                .orElseThrow(() -> new UserNotExistException("User was not found"));

        if (user.getId().equals(userDto.getId()) || user.getRole().getName().equals("ADMIN")) {
            if (!userFromDB.getEmail().equals(userDto.getEmail()) & findUserByEmail(userDto.getEmail()).isPresent()) {
                throw new EmailDuplicateException("Email is already exist");
            }
            if (findUserByUsername(userDto.getUsername()).isPresent() & !userFromDB.getUsername().equals(userDto.getUsername())) {
                throw new UsernameDuplicateException("Username is already exist");
            }
            updateUser(userDto);
        } else {
            throw new FaultPermissionException("You don`t have enough permission! Naughty boy!!!");
        }
    }

    @Transactional
    public void deactivateUser(Long id) {
        userRepository.deactivate(id);
    }

    @Transactional
    public void activateUser(Long id) {
        userRepository.activate(id);
    }
}
