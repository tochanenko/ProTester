package ua.project.protester.model.executable.result.subtype;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActionResultTechnicalExtra extends ActionResultSubtype {
    private String key;
    private String value;

    public ActionResultTechnicalExtra(Integer actionResultId, String key, String value) {
        super(actionResultId);
        this.key = key;
        this.value = value;
    }
}
