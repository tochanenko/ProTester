package ua.project.protester.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ua.project.protester.model.Role;
import ua.project.protester.model.Roles;
import ua.project.protester.model.UserDto;
import ua.project.protester.request.UserRequest;
import ua.project.protester.service.UserService;
import ua.project.protester.utils.UserMapper;

@RestController("/api")
public class RegistrationController {

    private final UserMapper userMapper;
    private final UserService userService;

    @Autowired
    public RegistrationController(UserMapper userMapper, UserService userService) {
        this.userMapper = userMapper;
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<String> registrate(@RequestBody UserRequest userRequest) {

        UserDto userDto  = userMapper.toDtoFromRequest(userRequest);

        userDto.setRoles(new Role(Roles.ROLE_ENGINEER.name()));

        return userService.createUser(userDto);
    }
}

