package ua.project.protester.model.executable.result.subtype;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ua.project.protester.exception.executable.action.ActionExecutionException;
import ua.project.protester.model.executable.result.ActionResultDto;

@Getter
@Setter
@AllArgsConstructor
public class ActionResultUiDto extends ActionResultDto {
    private String path;

    public ActionResultUiDto(ActionExecutionException e, String path) {
        super(e);
        this.path = path;
    }

    public ActionResultUiDto(ActionResultDto that, String path) {
        super(that);
        this.path = path;
    }
}
