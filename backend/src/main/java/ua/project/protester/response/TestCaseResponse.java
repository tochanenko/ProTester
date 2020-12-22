package ua.project.protester.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestCaseResponse {

    private Long id;

    private String name;

    private String description;

    private Long projectId;

    private Long authorId;

    private Long scenarioId;

    private List<DataSetResponse> dataSetResponseList;


}
