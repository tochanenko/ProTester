package ua.project.protester.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestCase {

    private Long id;

    private String name;

    private String description;

    private Long projectId;

    private Long authorId;

    private Long scenarioId;

    private List<DataSet> dataSetList = new ArrayList<>();

}
