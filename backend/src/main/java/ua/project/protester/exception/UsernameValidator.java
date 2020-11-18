package ua.project.protester.exception;

import org.springframework.beans.factory.annotation.Autowired;
import ua.project.protester.annotation.UniqueUsername;
import ua.project.protester.service.UserService;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UsernameValidator implements ConstraintValidator<UniqueUsername, String> {

    private final UserService userService;

    @Autowired
    public UsernameValidator(UserService userService) {
        this.userService = userService;
    }

    public void initialize(UniqueUsername uniqueUsername) {
    }

    public boolean isValid(String username, ConstraintValidatorContext constraintValidatorContext) {
        return userService.findUserByUsername(username) == null;
    }
}
