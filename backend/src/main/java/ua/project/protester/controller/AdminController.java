package ua.project.protester.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ua.project.protester.response.UserResponse;
import ua.project.protester.service.UserService;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private UserService userService;

    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public UserResponse findUserByUsername(@RequestParam String username) {
        return userService.findUserByName(username);
    }

    @GetMapping
    public UserResponse findUserByFullName(@RequestParam String fullName) {
        return userService.findUserByFullName(fullName);
    }

    @GetMapping
    public UserResponse findUserByRoleName(@RequestParam String roleName) {
        return userService.findUserByRolename(roleName);
    }

}
