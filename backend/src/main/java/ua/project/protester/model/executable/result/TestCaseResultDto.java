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
}
