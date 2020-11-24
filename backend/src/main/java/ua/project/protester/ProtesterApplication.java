package ua.project.protester;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import ua.project.protester.repository.ActionRepository;

@SpringBootApplication
public class ProtesterApplication {

    public static void main(final String[] args) {
        SpringApplication.run(ProtesterApplication.class, args);
    }

    @Bean
    public ActionRepository getActionsRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate, Environment env) {
        ActionRepository actionRepository = new ActionRepository(namedParameterJdbcTemplate, env);
        actionRepository.syncWithDb();
        return actionRepository;
    }

}
