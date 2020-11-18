package ua.project.protester.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.project.protester.model.User;
import ua.project.protester.repository.UserRepository;
import ua.project.protester.request.UserCreationRequestDto;
import ua.project.protester.request.UserModificationDto;
import ua.project.protester.response.UserResponse;
import ua.project.protester.utils.UserMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final RoleService roleService;

    @Autowired
    public UserService(UserMapper userMapper, UserRepository userRepository, RoleService roleService) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
        this.roleService = roleService;
    }

    public int createUser(UserCreationRequestDto userRequest) {
        User user = userMapper.toUserFromUserRequest(userRequest);
        user.setRole(roleService.findRoleByName(user.getRole().getName()));
        user.setActive(true);
        return userRepository.save(user);
    }

    public User findUserById(Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            Long roleId = user.getRole().getId();
            user.setRole(roleService.findRoleById(roleId));
            return user;
        }
        return null;
    }

    public User findUserByEmail(String email) {
        User user = userRepository.findUserByEmail(email).orElse(null);
        if (user != null) {
            Long roleId = user.getRole().getId();
            user.setRole(roleService.findRoleById(roleId));
            return user;
        }
        return null;
    }

    public User findUserByUsername(String username) {
        User user = userRepository.findUserByUsername(username).orElse(null);
        if (user != null) {
            Long roleId = user.getRole().getId();
            user.setRole(roleService.findRoleById(roleId));
            return user;
        }
        return null;
    }

    public void updateUser(UserModificationDto userDto) {
        User user = userMapper.toUserFromUserModificationDto(userDto);
        user.setRole(roleService.findRoleByName(user.getRole().getName()));
        userRepository.update(user);
    }

    public void deleteUser(User user) {
       user.getRole().getUsers().remove(user);
       userRepository.delete(user);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public UserResponse getUser(Long id) {
        return  userMapper.toUserRest(findUserById(id));
    }

    public List<UserResponse> getAllUsers() {
        return findAll().stream().map(userMapper::toUserRest).collect(Collectors.toList());
    }

}
