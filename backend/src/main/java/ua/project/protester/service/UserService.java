package ua.project.protester.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ua.project.protester.db.MockDB;
import ua.project.protester.exceptions.UserServiceException;
import ua.project.protester.model.Role;
import ua.project.protester.model.User;
import ua.project.protester.model.UserDto;
import ua.project.protester.repository.RoleRepository;
import ua.project.protester.repository.UserRepository;
import ua.project.protester.utils.UserMapper;

@Service
public class UserService {
    private UserMapper userMapper;
    private UserRepository userRepository;
    private RoleRepository roleRepository;

    @Autowired
    public UserService(UserMapper userMapper, UserRepository userRepository, RoleRepository roleRepository) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public User findUserByEmail(String email){
        return userRepository.findUserByEmail(email).orElseThrow(NullPointerException::new);
    }

    public User findUserById(Long id){
       return userRepository.findUserById(id).orElseThrow(NullPointerException::new);
    }

    public ResponseEntity<String> createUser(UserDto userDto){
        User entity=userMapper.toEntity(userDto);
        return userRepository.createUser(entity);
    }
}
