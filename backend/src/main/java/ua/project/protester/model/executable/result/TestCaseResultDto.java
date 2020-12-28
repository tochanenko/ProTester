package ua.project.protester.model.executable.result;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ua.project.protester.model.TestCase;
import ua.project.protester.model.User;

import java.time.OffsetDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TestCaseResultDto {
    private Integer id;
    private User user;
    private TestCase testCase;
    private ResultStatus status;
    private OffsetDateTime startDate;
    private OffsetDateTime endDate;
    private List<ActionResultDto> innerResults;
    private Long runResultId;

    public TestCaseResultDto(User user, TestCase testCase) {
        this.user = user;
        this.testCase = testCase;
        this.status = ResultStatus.IN_PROGRESS;
        this.startDate = OffsetDateTime.now();
    }

    public TestCaseResultDto(Integer id, User user, TestCase testCase, ResultStatus status, OffsetDateTime startDate, OffsetDateTime endDate, List<ActionResultDto> innerResults) {
        this.id = id;
        this.user = user;
        this.testCase = testCase;
        this.status = status;
        this.startDate = startDate;
        this.endDate = endDate;
        this.innerResults = innerResults;
    }
}
