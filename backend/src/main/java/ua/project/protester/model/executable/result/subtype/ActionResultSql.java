package ua.project.protester.model.executable.result.subtype;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ActionResultSql extends ActionResultSubtype {
    private String connectionUrl;
    private String username;
    private String query;

    public ActionResultSql(Integer actionResultId, String connectionUrl, String username, String query) {
        super(actionResultId);
        this.connectionUrl = connectionUrl;
        this.username = username;
        this.query = query;
    }
}
