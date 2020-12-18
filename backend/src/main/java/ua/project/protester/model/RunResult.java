package ua.project.protester.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RunResult {

    private Long id;

    private List<Integer> testCaseResult;

    private Long userId;

    //first - testCaseResultId, second - wrapper
    private Map<Integer, List<ActionWrapper>> actionWrapper = new HashMap<>();

    public RunResult(Long id, Long userId) {
        this.id = id;
        this.userId = userId;
    }

}
