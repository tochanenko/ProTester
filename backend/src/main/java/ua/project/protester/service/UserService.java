package ua.project.protester.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.project.protester.model.User;
import ua.project.protester.repository.UserRepository;
import ua.project.protester.request.UserCreationRequestDto;
import ua.project.protester.response.UserResponse;
import ua.project.protester.utils.UserMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserMapper userMapper;
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserMapper userMapper, UserRepository userRepository) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
    }

    public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email).orElse(null);
    }

    public User findUserById(Long id) {
       return userRepository.findUserById(id).orElse(null);
    }

    public List<UserResponse> findAllUsers() {
        return userRepository.findAllUsers().stream().map(userMapper::toUserRest).collect(Collectors.toList());
    }

    public User createUser(UserCreationRequestDto userRequest) {
        User user = userMapper.toUserFromUserRequest(userRequest);
        return userRepository.createUser(user);
    }

    public List<User> findUserByRoleId(Long id) {
        return userRepository.findUserByRoleId(id);
    }

}
