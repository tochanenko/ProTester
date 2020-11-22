package ua.project.protester;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ua.project.protester.model.User;
import ua.project.protester.repository.TestRepository;

@SpringBootApplication
public class ProtesterApplication {

    public static void main(final String[] args) {
        SpringApplication.run(ProtesterApplication.class, args);
    }

}
