package ua.project.protester.annotation;

import ua.project.protester.exception.UserEmailValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UserEmailValidator.class)
@Documented
public @interface NotExistingEmail {

    String message() default "User already exist";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
