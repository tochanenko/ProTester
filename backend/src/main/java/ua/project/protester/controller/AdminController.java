package ua.project.protester.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.web.bind.annotation.*;
import ua.project.protester.model.User;
import ua.project.protester.request.UserModificationDto;
import ua.project.protester.response.UserResponse;
import ua.project.protester.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@EnableGlobalMethodSecurity(prePostEnabled = true)
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminController {

    private UserService userService;

    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/username")
    public UserResponse findUserByUsername(@RequestParam(name = "username") String username) {
        return userService.findUserByName(username);
    }

    @GetMapping("/name")
    public List<UserResponse> findUsersByName(@RequestParam(name = "name") String name) {
        return userService.findUsersByName(name);
    }

    @GetMapping("/surname")
    public List<UserResponse> findUsersBySurname(@RequestParam(name = "surname") String name) {
        return userService.findUsersBySurname(name);
    }

    @GetMapping("/email")
    public UserResponse findUsersByEmail(@RequestParam(name = "email") String email) {
        return userService.findUsersByEmail(email);
    }

    @GetMapping("/role")
    public List<UserResponse> findUsersByRole(@RequestParam(name = "role") String role) {
        return userService.findUsersByRoleName(role);
    }

    @GetMapping("/profiles")
    public List<UserResponse> findAll() {
        return userService.getAllUsers();
    }

    @GetMapping("/profiles/{id}")
    public UserResponse findUserById(@PathVariable Long id) {
        return userService.getUser(id);
    }

    @PutMapping("/profiles/{id}")
    public ResponseEntity<String> updateUser(@PathVariable Long id, @RequestBody UserModificationDto userDto) {
        if (userService.findUserById(id) == null) {
            return new ResponseEntity<>("User doesn`t exist", HttpStatus.NOT_FOUND);
        }
        userDto.setId(id);
        userService.modifyUser(userDto);
        return new ResponseEntity<>("User was successfully modified", HttpStatus.OK);
    }

    @PostMapping("/profiles/deactivate/{id}")
    public ResponseEntity<String> deactivate(@PathVariable Long id) {
        User user = userService.findUserById(id);
        if (user == null) {
            return new ResponseEntity<>("User does`nt exist", HttpStatus.NOT_FOUND);
        }
        userService.deactivateUser(id);
        return new ResponseEntity<>("User was deactivated!", HttpStatus.OK);
    }
}
