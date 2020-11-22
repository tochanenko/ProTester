package ua.project.protester.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ua.project.protester.model.User;
import ua.project.protester.utils.UserRowMapper;

@Repository
public class TestRepository extends AbstractRepository<User> {

    private  UserRowMapper userRowMapper;
    private  Environment environment;
    private  NamedParameterJdbcTemplate jdbcTemplate;

    {
        super.environment=environment;
        super.rowMapper=userRowMapper;
        super.namedJdbcTemplate=jdbcTemplate;
        super.template=new String[]{"findUserById"};
    }

    @Autowired
    public TestRepository(NamedParameterJdbcTemplate namedJdbcTemplate, Environment environment, RowMapper<User> rowMapper) {
        super(namedJdbcTemplate, environment, rowMapper);
    }
}
