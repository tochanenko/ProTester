package ua.project.protester.annotation;

import ua.project.protester.model.executable.ExecutableComponentType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Action {
    String name();
    ExecutableComponentType type();
    String description() default "Empty description";
    String[] parameterNames() default {};
}
