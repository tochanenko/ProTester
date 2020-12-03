package ua.project.protester.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestCaseRequest {

    private Long id;

    private String name;

    private String description;

    private Long projectId;

    private Long authorId;

    private Long scenarioId;

    private List<Long> dataSetId = new ArrayList<>();
}
