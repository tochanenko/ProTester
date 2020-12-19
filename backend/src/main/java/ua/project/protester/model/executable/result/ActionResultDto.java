package ua.project.protester.model.executable.result;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ua.project.protester.exception.executable.action.ActionExecutionException;
import ua.project.protester.model.executable.AbstractAction;

import java.time.OffsetDateTime;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class ActionResultDto {
    protected Integer id;
    protected AbstractAction action;
    protected OffsetDateTime startDate;
    protected OffsetDateTime endDate;
    protected ResultStatus status;
    protected Map<String, String> inputParameters;
    protected String message;

    public ActionResultDto() {
        status = ResultStatus.PASSED;
    }

    public ActionResultDto(ActionExecutionException e) {
        status = ResultStatus.FAILED;
        message = e.getMessage();
    }

    public ActionResultDto(ActionResultDto that) {
        this.id = that.id;
        this.action = that.action;
        this.startDate = that.startDate;
        this.endDate = that.endDate;
        this.status = that.status;
        this.inputParameters = that.inputParameters;
        this.message = that.message;
    }
}
