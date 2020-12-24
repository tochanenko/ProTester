package ua.project.protester.model.executable.result;

public enum ResultStatus {
    PASSED,
    FAILED,
    IN_PROGRESS
}
// test-case-request-test-case-result
// requestId, test-case-result-id, unique
//       scenario id : 2 -> test_case_results {1,2,3}
//       scenario id : 3 -> test_case_results {4,5,6}
//       forward
//       getTestCaseResultBeScenarioIdAndUniqueNumber(scenarioId, uniqueNumber)
