package ua.project.protester.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.project.protester.model.User;
import ua.project.protester.request.UserCreationRequest;
import ua.project.protester.repository.UserRepository;

@Service
public class UserService {
    private final ModelMapper userMapper;
    private final UserRepository userRepository;

    @Autowired
    public UserService(ModelMapper userMapper, UserRepository userRepository) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
    }

    public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email).orElseThrow(NullPointerException::new);
    }

    public User findUserById(Long id) {
       return userRepository.findUserById(id).orElseThrow(NullPointerException::new);
    }

    public User createUser(UserCreationRequest userRequest) {
        User user = userMapper.map(userRequest, User.class);
        return userRepository.createUser(user);
    }
}
