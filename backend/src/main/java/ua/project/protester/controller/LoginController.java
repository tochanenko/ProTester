package ua.project.protester.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.project.protester.model.UserDto;
import ua.project.protester.response.UserLoginResponse;
import ua.project.protester.service.AuthorizationService;

@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
@CrossOrigin("http://localhost:4200")
public class LoginController {


    private final AuthorizationService authorizationService;

    @PostMapping("/signin")
    public ResponseEntity<UserLoginResponse> login(@RequestBody UserDto userDto) {

        System.out.println(userDto);
        return ResponseEntity.ok(authorizationService.authenticate(userDto));
    }
}

