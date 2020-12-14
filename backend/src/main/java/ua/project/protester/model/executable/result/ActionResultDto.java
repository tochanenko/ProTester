package ua.project.protester.model.executable.result;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ua.project.protester.exception.executable.action.ActionExecutionException;
import ua.project.protester.model.executable.AbstractAction;

import java.time.OffsetDateTime;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class ActionResultDto {
    protected Integer id;
    protected AbstractAction action;
    protected OffsetDateTime startDate;
    protected OffsetDateTime endDate;
    protected ResultStatus status;
    protected Map<String, String> inputParameters;
    protected ActionExecutionException exception;

    public ActionResultDto() {
        status = ResultStatus.PASSED;
    }

    public ActionResultDto(ActionExecutionException e) {
        status = ResultStatus.FAILED;
        exception = e;
    }

    public void init(ActionResultDto that) {
        this.id = that.id;
        this.action = that.action;
        this.startDate = that.startDate;
        this.endDate = that.endDate;
        this.status = that.status;
        this.exception = that.exception;
    }
}
