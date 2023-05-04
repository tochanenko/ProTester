package ua.project.protester.action;

import org.openqa.selenium.WebDriver;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.client.RestTemplate;
import ua.project.protester.annotation.Action;
import ua.project.protester.exception.executable.action.ActionExecutionException;
import ua.project.protester.model.Environment;
import ua.project.protester.model.executable.AbstractAction;
import ua.project.protester.model.executable.ExecutableComponentType;
import ua.project.protester.model.executable.result.subtype.ActionResultSqlDto;
import ua.project.protester.model.executable.result.subtype.SqlColumnDto;
import ua.project.protester.utils.SqlConverter;

import java.util.*;

@Action(
        name = "Execute ${query} and return table",
        type = ExecutableComponentType.SQL,
        description = "Execute the specified query and return result table",
        parameterNames = {"query"}
)
public class SqlAction extends AbstractAction {

    @Override
    protected ActionResultSqlDto logic(Map<String, String> params, Map<String, String> context, WebDriver driver, JdbcTemplate jdbcTemplate, Environment environment, RestTemplate restTemplate) {
        String query = params.get("query");
        try {
            List<SqlColumnDto> table = jdbcTemplate.query(
                    query,
                    SqlConverter::convertResultSetToTable);

            if (table != null) {
                return new ActionResultSqlDto(
                        environment.getUrl(),
                        environment.getUsername(),
                        query,
                        table);
            }

            throw new Exception("Query returned null");
        } catch (Exception e) {
            return new ActionResultSqlDto(
                    new ActionExecutionException(e.getMessage()),
                    environment.getUrl(),
                    environment.getUsername(),
                    query,
                    Collections.emptyList());
        }
    }
}
