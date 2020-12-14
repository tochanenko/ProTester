package ua.project.protester.model.executable.result.subtype;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActionResultUi extends ActionResultSubtype {
    private String path;

    public ActionResultUi(Integer actionResultId, String path) {
        super(actionResultId);
        this.path = path;
    }
}
