package ua.project.protester.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ua.project.protester.request.TestCaseRequest;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class RunTestCase {

    private Long id;

    private List<TestCaseRequest> testCaseRequestList;

    private Long userId;

    public RunTestCase() {
    }

    public RunTestCase(List<TestCaseRequest> testCaseRequestList, Long userId) {
        this.testCaseRequestList = testCaseRequestList;
        this.userId = userId;
    }
}
