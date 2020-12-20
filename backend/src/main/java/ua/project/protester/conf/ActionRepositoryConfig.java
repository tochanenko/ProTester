package ua.project.protester.conf;

import org.reflections.Reflections;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import ua.project.protester.repository.ActionRepository;

@Configuration
@PropertySource("classpath:queries/action.properties")
public class ActionRepositoryConfig {
    @Bean
    public ActionRepository actionRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate, Environment env, Reflections reflections) {
        ActionRepository actionRepository = new ActionRepository(namedParameterJdbcTemplate, env, reflections);
        actionRepository.initialize();
        return actionRepository;
    }

    @Bean
    public Reflections reflections() {
        return new Reflections("ua.project.protester.action");
    }
}
