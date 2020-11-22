package ua.project.protester.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ua.project.protester.model.User;
import ua.project.protester.request.UserModificationDto;
import ua.project.protester.response.UserResponse;
import ua.project.protester.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
public class UserApiController {

    private UserService userService;

    @Autowired
    public UserApiController(UserService userService) {
        this.userService = userService;
    }

    @PutMapping(path = "/user/{id}")
    public ResponseEntity<String> updateUser(@PathVariable(name = "id") Long id, @RequestBody @Valid UserModificationDto user) {
        User oldUser = userService.findUserById(id);
        if (oldUser == null) {
            return new ResponseEntity<>("User doesn`t exist", HttpStatus.NOT_FOUND);
        }
        user.setId(id);
        userService.updateUser(user);
        return new ResponseEntity<>("User was successfully updated", HttpStatus.OK);
    }

    @DeleteMapping(path = "/user/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable(name = "id") Long id) {
        User oldUser = userService.findUserById(id);
        if (oldUser == null) {
            return new ResponseEntity<>("User doesn`t exist", HttpStatus.NOT_FOUND);
        }
        userService.deleteUser(oldUser);
        return new ResponseEntity<>("User was successfully deleted", HttpStatus.OK);
    }


    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping(path = "/user/{id}")
    public UserResponse getUser(@PathVariable(name = "id") Long id) {
        return userService.getUser(id);
    }


    @GetMapping(path = "/users")
    public List<UserResponse> getAllUsers() {
        return userService.getAllUsers();
    }
}
