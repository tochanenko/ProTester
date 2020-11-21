package ua.project.protester.validators;

import org.springframework.beans.factory.annotation.Autowired;
import ua.project.protester.annotation.UniqueEmail;
import ua.project.protester.service.UserService;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UserEmailValidator  implements ConstraintValidator<UniqueEmail, String> {

    private UserService userService;


    @Autowired
    public UserEmailValidator(UserService userService) {
        this.userService = userService;
    }

    public void initialize(UniqueEmail notExistingEmail) {
    }


    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
     return userService.findUserByEmail(email) == null;
    }
}
