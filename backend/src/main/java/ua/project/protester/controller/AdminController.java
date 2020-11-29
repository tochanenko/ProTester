package ua.project.protester.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ua.project.protester.request.UserModificationDto;
import ua.project.protester.response.UserResponse;
import ua.project.protester.service.UserService;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminController {

    private UserService userService;

    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/findUsersBy")
    public List<UserResponse> findUsersByParam(@RequestParam(required = false) String name,
                                              @RequestParam(required = false)String surname,
                                              @RequestParam(required = false)String role,
                                              @RequestParam(required = false)String email,
                                              @RequestParam(required = false)String username) {

        if (name != null) {
            return userService.findUsersByName(name);
        }
        if (surname != null) {
            return userService.findUsersBySurname(surname);
        }
        if (role != null) {
            return userService.findUsersByRoleName(role);
        }

        if (email != null) {
            return List.of(userService.findUsersByEmail(email));
        }

        if (username != null) {
            return List.of(userService.findUsersByUsername(username));
        }

        return Collections.emptyList();
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
        if (userService.findUserById(id).isEmpty()) {
            return new ResponseEntity<>("User doesn`t exist", HttpStatus.NOT_FOUND);
        }
        userDto.setId(id);
        userService.modifyUser(userDto);
        return new ResponseEntity<>("User was successfully modified", HttpStatus.OK);
    }

    @PostMapping("/profiles/{id}")
    public ResponseEntity<String> deactivate(@PathVariable Long id) {
        if (userService.findUserById(id).isEmpty()) {
            return new ResponseEntity<>("User does`nt exist", HttpStatus.NOT_FOUND);
        }
        userService.deactivateUser(id);
        return new ResponseEntity<>("User was deactivated!", HttpStatus.OK);
    }
}
