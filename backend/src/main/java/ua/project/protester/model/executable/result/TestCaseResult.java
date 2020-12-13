package ua.project.protester.model.executable.result;

import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
public class TestCaseResult {
    private Integer id;
    private Integer userId;
    private Integer testCaseId;
    private Integer statusId;
    private OffsetDateTime startDate;
    private OffsetDateTime endDate;
}
