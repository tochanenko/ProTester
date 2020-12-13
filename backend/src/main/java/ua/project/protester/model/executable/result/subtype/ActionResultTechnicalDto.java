package ua.project.protester.model.executable.result.subtype;

import lombok.Getter;
import lombok.Setter;
import ua.project.protester.exception.executable.action.ActionExecutionException;
import ua.project.protester.model.executable.result.ActionResultDto;

import java.util.Map;

@Getter
@Setter
public class ActionResultTechnicalDto extends ActionResultDto {
    private Map<String, String> extra;

    public ActionResultTechnicalDto() {
        super();
    }

    public ActionResultTechnicalDto(ActionExecutionException e) {
        super(e);
    }
}
