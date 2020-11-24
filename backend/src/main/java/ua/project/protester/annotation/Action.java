package ua.project.protester.annotation;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Action {
    String name() default "";
    int type() default 1;
    String description() default "Empty description";
}
