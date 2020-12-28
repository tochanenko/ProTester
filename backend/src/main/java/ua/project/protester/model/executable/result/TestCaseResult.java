package ua.project.protester.model.executable.result;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TestCaseResult {
    private Integer id;
    private Long userId;
    private Long testCaseId;
    private Integer statusId;
    private OffsetDateTime startDate;
    private OffsetDateTime endDate;
    private Long runResultId;
}
