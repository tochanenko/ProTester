package ua.project.protester.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import ua.project.protester.repository.ActionDeclarationRepository;

@Configuration
@PropertySource("classpath:queries/action-declaration.properties")
public class ActionDeclarationRepositoryConfig {
    @Bean
    public ActionDeclarationRepository actionDeclarationRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate, Environment env) {
        ActionDeclarationRepository actionDeclarationRepository = new ActionDeclarationRepository(namedParameterJdbcTemplate, env);
        actionDeclarationRepository.syncWithDb();
        return actionDeclarationRepository;
    }
}
