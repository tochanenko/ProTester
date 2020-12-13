package ua.project.protester.model.executable.result;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ua.project.protester.model.TestCase;
import ua.project.protester.model.executable.ExecutableComponentType;

import java.time.OffsetDateTime;
import java.util.Map;

@Getter
@Setter
@ToString
public class ActionResult {
    private Integer id;
    private Integer testCaseResultId;
    private Integer actionId;
    private OffsetDateTime startDate;
    private OffsetDateTime endDate;
    private Integer statusId;
    private String message;
}
