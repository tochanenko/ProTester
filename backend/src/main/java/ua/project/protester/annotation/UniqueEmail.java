package ua.project.protester.annotation;

import ua.project.protester.validators.UserEmailValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UserEmailValidator.class)
@Documented
public @interface UniqueEmail {

    String message() default "User already exist";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
