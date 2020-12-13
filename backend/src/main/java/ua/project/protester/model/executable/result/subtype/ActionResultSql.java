package ua.project.protester.model.executable.result.subtype;

import lombok.Getter;
import lombok.Setter;
import ua.project.protester.model.executable.result.ActionResult;

@Getter
@Setter
public class ActionResultSql extends ActionResultSubtype {
    private String connectionUrl;
    private String username;
    private String query;
}
