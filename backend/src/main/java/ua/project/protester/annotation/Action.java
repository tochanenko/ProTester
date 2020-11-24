package ua.project.protester.annotation;

import ua.project.protester.model.ActionType;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Action {
    String name() default "";
    ActionType type() default ActionType.TECHNICAL;
    String description() default "Empty description";
    String[] parameterNames() default {};
}
