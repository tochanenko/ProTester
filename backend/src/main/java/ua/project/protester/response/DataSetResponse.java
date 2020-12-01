package ua.project.protester.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataSetResponse {

    private Long id;

    private String name;

    private String description;

    private Map<String, String> dataset;

    private List<Long> testScenarios;
}
