package ua.project.protester.model.executable.result;

import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.List;

@Getter
@Setter
public class TestCaseResult {
    private Integer id;
    private Integer userId;
    private Integer testCaseId;
    private Integer statusId;
    private ResultStatus status;
    private OffsetDateTime startDate;
    private OffsetDateTime endDate;
    private List<ActionResult> innerResults;
}
