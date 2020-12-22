package ua.project.protester.exception.executable.scenario;

import lombok.Getter;
import ua.project.protester.response.LightTestCaseResponse;

import java.util.List;

@Getter
public class UsedTestScenarioDeleteException extends Exception {

    private final List<LightTestCaseResponse> testCases;

    public UsedTestScenarioDeleteException(String message, List<LightTestCaseResponse> testCases) {
        super(message);
        this.testCases = testCases;
    }
}
