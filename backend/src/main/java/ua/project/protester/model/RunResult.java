package ua.project.protester.model;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RunResult {

    private Long id;

    private List<TestCaseWrapperResult> testCaseResults;

    public RunResult(Long id) {
        this.id = id;
    }

    //first - testCaseResultId, second - wrapper
    //private Map<Integer, List<ActionWrapper>> actionWrapper = new HashMap<>();

//    public RunResult(Long id, Long userId) {
//        this.id = id;
//        this.userId = userId;
//    }



}
