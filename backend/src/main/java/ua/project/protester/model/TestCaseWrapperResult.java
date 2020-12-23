package ua.project.protester.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestCaseWrapperResult {

    private Integer id;

    private List<ActionWrapper> actionWrapperList;

    private Integer scenarioId;

    private Integer testResultId;

    public TestCaseWrapperResult(Integer id, Integer scenarioId, Integer testResultId) {
        this.id = id;
        this.scenarioId = scenarioId;
        this.testResultId = testResultId;
    }
}
