package ua.project.protester.exception;

import org.springframework.beans.factory.annotation.Autowired;
import ua.project.protester.annotation.NotExistingEmail;
import ua.project.protester.service.UserService;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UserEmailValidator  implements ConstraintValidator<NotExistingEmail, String> {

    private final UserService userService;

    @Autowired
    public UserEmailValidator(UserService userService) {
        this.userService = userService;
    }

    public void initialize(NotExistingEmail notExistingEmail) {
    }

    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
        return userService.findUserByEmail(email) == null;
    }
}
