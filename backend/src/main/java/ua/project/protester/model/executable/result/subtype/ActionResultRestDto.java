package ua.project.protester.model.executable.result.subtype;

import lombok.Getter;
import lombok.Setter;
import ua.project.protester.exception.executable.action.ActionExecutionException;
import ua.project.protester.model.executable.result.ActionResultDto;

@Getter
@Setter
public class ActionResultRestDto extends ActionResultDto {
    private String request;
    private String response;
    private Integer statusCode;

    public ActionResultRestDto() {
        super();
    }

    public ActionResultRestDto(ActionExecutionException e) {
        super(e);
    }
}
