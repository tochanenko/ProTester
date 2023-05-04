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

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Action(
        name = "Set ${key} = result of ${query} execution",
        type = ExecutableComponentType.SQL,
        description = "Execute the specified query and save result to context. Result table must contain exactly 1 column and 1 row",
        parameterNames = {"query", "key"}
)
public class SqlSaveAction extends AbstractAction {
    @Override
    protected ActionResultSqlDto logic(Map<String, String> params, Map<String, String> context, WebDriver driver, JdbcTemplate jdbcTemplate, Environment environment, RestTemplate restTemplate) {
        String query = params.get("query");
        try {
            List<SqlColumnDto> table = jdbcTemplate.query(
                    query,
                    SqlConverter::convertResultSetToTable);

            if (table == null) {
                throw new Exception("Query returned null");
            }

            int columnsNum = table.size();
            if (columnsNum != 1) {
                throw new Exception("Table must have 1 column, but " + columnsNum + " found");
            }

            List<String> firstColumnRows = table.get(0).getRows();
            int rowsNum = firstColumnRows.size();
            if (rowsNum != 1) {
                throw new Exception("Table must have 1 row, but " + rowsNum + " found");
            }

            context.put(params.get("key"), firstColumnRows.get(0));

            return new ActionResultSqlDto(
                    environment.getUrl(),
                    environment.getUsername(),
                    query,
                    table);
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
