package ua.project.protester.model.executable.result.subtype;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActionResultSubtype {
    protected Integer id;
    protected Integer actionResultId;

    public ActionResultSubtype(Integer actionResultId) {
        this.actionResultId = actionResultId;
    }
}
