package ua.project.protester.model.executable.result;

import lombok.Getter;
import lombok.Setter;
import ua.project.protester.exception.executable.action.ActionExecutionException;
import ua.project.protester.model.executable.AbstractAction;

import java.time.OffsetDateTime;

@Getter
@Setter
public class ActionResultDto {
    protected AbstractAction action;
    protected OffsetDateTime startDate;
    protected OffsetDateTime endDate;
    protected ResultStatus status;
    protected ActionExecutionException exception;

    public ActionResultDto() {
        status = ResultStatus.PASSED;
    }

    public ActionResultDto(ActionExecutionException e) {
        status = ResultStatus.FAILED;
        exception = e;
    }
}
