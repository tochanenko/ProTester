package ua.project.protester.model.executable.result.subtype;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ua.project.protester.exception.executable.action.ActionExecutionException;
import ua.project.protester.model.executable.result.ActionResultDto;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ActionResultSqlDto extends ActionResultDto {
    private String connectionUrl;
    private String username;
    private String query;
    private List<SqlColumnDto> columns;

    public ActionResultSqlDto(ActionExecutionException e, String connectionUrl, String username, String query, List<SqlColumnDto> columns) {
        super(e);
        this.connectionUrl = connectionUrl;
        this.username = username;
        this.query = query;
        this.columns = columns;
    }
}
