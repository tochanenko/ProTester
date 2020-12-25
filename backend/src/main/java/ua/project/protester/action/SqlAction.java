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

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;

@Action(
        name = "Execute ${query} and return table",
        type = ExecutableComponentType.SQL,
        description = "Execute the specified query and return result table",
        parameterNames = {"query"}
)
public class SqlAction extends AbstractAction {

    private static List<SqlColumnDto> extractTableFromResultSet(ResultSet resultSet) {
        try {
            ResultSetMetaData metaData = resultSet.getMetaData();

            List<SqlColumnDto> sqlTable = new ArrayList<>(metaData.getColumnCount());

            for (int i = 0; i < metaData.getColumnCount(); i++) {
                sqlTable.set(i, new SqlColumnDto(
                        metaData.getColumnName(i),
                        new LinkedList<>()));
            }

            while (resultSet.next()) {
                for (int i = 0; i < sqlTable.size(); i++) {
                    sqlTable.get(i).getRows().add(resultSet.getString(i + 1));
                }
            }

            return sqlTable;
        } catch (SQLException e) {
            return Collections.emptyList();
        }
    }

    @Override
    protected ActionResultSqlDto logic(Map<String, String> params, Map<String, String> context, WebDriver driver, JdbcTemplate jdbcTemplate, Environment environment, RestTemplate restTemplate) {
        String query = params.get("query");
        try {
            List<SqlColumnDto> table = jdbcTemplate.query(
                    query,
                    SqlAction::extractTableFromResultSet);

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
