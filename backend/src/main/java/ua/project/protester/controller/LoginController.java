package ua.project.protester.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.project.protester.model.UserDto;
import ua.project.protester.response.UserLoginResponse;
import ua.project.protester.service.UserService;

@RequestMapping("/api")
@RestController
public class LoginController {


    private UserService userService;

    @Autowired
    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signin")
    public ResponseEntity<UserLoginResponse> login(@RequestBody UserDto userDto) {

        return ResponseEntity.ok(userService.authenticate(userDto));
    }
}

