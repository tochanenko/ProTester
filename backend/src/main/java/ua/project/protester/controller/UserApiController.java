package ua.project.protester.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.project.protester.request.UserModificationDto;
import ua.project.protester.response.UserResponse;
import ua.project.protester.service.UserService;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
public class UserApiController {

    private UserService userService;

    @Autowired
    public UserApiController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public UserResponse currentUserDetails() {
        return userService.currentUserDetails();
    }

    @PutMapping(path = "/profile/{id}")
    public ResponseEntity<String> updateUser(@PathVariable Long id, @RequestBody @Valid UserModificationDto user) {
        if (userService.findUserById(id) == null) {
            return new ResponseEntity<>("User doesn`t exist!", HttpStatus.NOT_FOUND);
        }
        user.setId(id);
        userService.modifyUser(user);
        return new ResponseEntity<>("User was successfully updated", HttpStatus.OK);
    }

}
