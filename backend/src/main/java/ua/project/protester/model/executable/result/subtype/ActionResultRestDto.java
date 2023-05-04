package ua.project.protester.model.executable.result.subtype;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ua.project.protester.exception.executable.action.ActionExecutionException;
import ua.project.protester.model.executable.result.ActionResultDto;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ActionResultRestDto extends ActionResultDto {
    private String request;
    private String response;
    private Integer statusCode;

    public ActionResultRestDto(ActionExecutionException e, String request, String response, Integer statusCode) {
        super(e);
        this.request = request;
        this.response = response;
        this.statusCode = statusCode;
    }

    public ActionResultRestDto(ActionResultDto that, String request, String response, Integer statusCode) {
        super(that);
        this.request = request;
        this.response = response;
        this.statusCode = statusCode;
    }
}
