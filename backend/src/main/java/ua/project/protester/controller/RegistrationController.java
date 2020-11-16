package ua.project.protester.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ua.project.protester.request.UserCreationRequest;
import ua.project.protester.service.UserService;

@RestController("/api")
public class RegistrationController {

    private final UserService userService;

    @Autowired
    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<String> registrate(@RequestBody @Validated UserCreationRequest userCreationRequest) {

        if (userService.createUser(userCreationRequest) == null) {
         return new ResponseEntity<>("User already exist", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("User was successfully created!", HttpStatus.CREATED);
    }

}

