package ua.project.protester.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.project.protester.exception.EmailDuplicateException;
import ua.project.protester.exception.FaultPermissionException;
import ua.project.protester.exception.MailSendException;
import ua.project.protester.exception.UsernameDuplicateException;
import ua.project.protester.model.User;
import ua.project.protester.repository.UserRepository;
import ua.project.protester.request.UserCreationRequestDto;
import ua.project.protester.request.UserModificationDto;
import ua.project.protester.response.UserResponse;
import ua.project.protester.utils.UserMapper;

import java.util.List;
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
    public int createUser(UserCreationRequestDto userRequest) throws MailSendException {
        User user = userMapper.toUserFromUserRequest(userRequest);
        user.setRole(roleService.findRoleByName(user.getRole().getName()));
        user.setActive(true);
        logger.info("Creating user {}", user);
        mailService.sendRegistrationCredentials(user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Transactional
    public void updateUser(UserModificationDto userDto) {
        User user = userMapper.toUserFromUserModificationDto(userDto);
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setRole(roleService.findRoleByName(user.getRole().getName()));
        logger.info("Updating user {}", user);
        userRepository.update(user);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("User doesn`t exist"));
        //user.getRole().getUsers().remove(user);
        userRepository.delete(user);
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
            logger.info("User with email {} was found {}", email, user);
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
            logger.info("User with username {} was found {}", username, user);
            return user;
        }
        return null;
    }

    public UserResponse getUser(Long id) {
        return userMapper.toUserRest(findUserById(id));
    }

    public UserResponse findUserByName(String username) {
        return userMapper.toUserRest(findUserByUsername(username));
    }

    public UserResponse findUsersByEmail(String email) {
        return userMapper.toUserRest(findUserByEmail(email));
    }

    @Transactional
    public List<User> findAll() {
        List<User> users = userRepository.findAll();
        users.forEach(user -> user.setRole(roleService.findRoleById(user.getRole().getId())));
        return users;
    }

    @Transactional
    public List<UserResponse> findUsersByRoleName(String roleName) {
        List<User> users = userRepository.findUserByRolename(roleName);
        users.forEach(user -> user.setRole(roleService.findRoleById(user.getRole().getId())));
        return users.stream().map(userMapper::toUserRest).collect(Collectors.toList());
    }

    @Transactional
    public List<UserResponse> findUsersByName(String name) {
        List<User> users = userRepository.findUsersByName(name);
        users.forEach(user -> user.setRole(roleService.findRoleById(user.getRole().getId())));
        return users.stream().map(userMapper::toUserRest).collect(Collectors.toList());
    }

    @Transactional
    public List<UserResponse> findUsersBySurname(String surname) {
        List<User> users = userRepository.findUsersBySurname(surname);
        users.forEach(user -> user.setRole(roleService.findRoleById(user.getRole().getId())));
        return users.stream().map(userMapper::toUserRest).collect(Collectors.toList());
    }

    public List<UserResponse> getAllUsers() {
        return findAll().stream().map(userMapper::toUserRest).collect(Collectors.toList());
    }

    private User currentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            return findUserByEmail(authentication.getName());
        }
        return null;
    }

    public UserResponse currentUserDetails() {
        return userMapper.toUserRest(currentUser());
    }

    public void modifyUser(UserModificationDto userDto) {
        User user = currentUser();
        User userFromDB = findUserById(userDto.getId());
        if (user != null && (user.getId().equals(userDto.getId()) || user.getRole().getName().equals("ADMIN"))) {
            if (!userFromDB.getEmail().equals(userDto.getEmail()) & findUserByEmail(userDto.getEmail()) != null) {
                throw new EmailDuplicateException("Email is already exist");
            }
            if (findUserByUsername(userDto.getUsername()) != null & !userFromDB.getUsername().equals(userDto.getUsername())) {
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
}
