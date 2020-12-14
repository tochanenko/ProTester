package ua.project.protester.model.executable.result.subtype;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SqlColumn {
    private Integer id;
    private Integer actionResultSqlId;
    private String name;
}
