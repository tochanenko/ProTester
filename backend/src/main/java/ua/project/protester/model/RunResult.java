package ua.project.protester.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RunResult {

    private Long id;

    private List<Integer> testCaseResult;

    private Long userId;

    public RunResult(Long id, Long userId) {
        this.id = id;
        this.userId = userId;
    }
}
