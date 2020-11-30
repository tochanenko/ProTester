package ua.project.protester.annotation;

import ua.project.protester.model.executable.ExecutableComponentType;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Action {
    String name() default "";
    ExecutableComponentType type() default ExecutableComponentType.TECHNICAL;
    String description() default "Empty description";
    String[] parameterNames() default {};
}
