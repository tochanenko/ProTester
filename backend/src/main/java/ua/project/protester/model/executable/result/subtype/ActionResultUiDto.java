package ua.project.protester.model.executable.result.subtype;

import lombok.Getter;
import lombok.Setter;
import ua.project.protester.exception.executable.action.ActionExecutionException;
import ua.project.protester.model.executable.result.ActionResultDto;

@Getter
@Setter
public class ActionResultUiDto extends ActionResultDto {
    private String path;

    public ActionResultUiDto() {
        super();
    }

    public ActionResultUiDto(ActionExecutionException e) {
        super(e);
    }
}
