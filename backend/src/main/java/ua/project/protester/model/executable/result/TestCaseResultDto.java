package ua.project.protester.model.executable.result;

import lombok.Getter;
import lombok.Setter;
import ua.project.protester.model.TestCase;
import ua.project.protester.model.User;

import java.time.OffsetDateTime;
import java.util.List;

@Getter
@Setter
public class TestCaseResultDto {
    private User user;
    private TestCase testCase;
    private ResultStatus status;
    private OffsetDateTime startDate;
    private OffsetDateTime endDate;
    private List<ActionResultDto> innerResults;
}
