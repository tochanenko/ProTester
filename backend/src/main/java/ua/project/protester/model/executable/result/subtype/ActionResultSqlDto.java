package ua.project.protester.model.executable.result.subtype;

import lombok.Getter;
import lombok.Setter;
import ua.project.protester.exception.executable.action.ActionExecutionException;
import ua.project.protester.model.executable.result.ActionResultDto;

import java.util.List;

@Getter
@Setter
public class ActionResultSqlDto extends ActionResultDto {
    private String connectionUrl;
    private String username;
    private String query;
    private List<SqlColumnDto> columns;

    public ActionResultSqlDto() {
        super();
    }

    public ActionResultSqlDto(ActionExecutionException e) {
        super(e);
    }
}
