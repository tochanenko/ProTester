package ua.project.protester.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataSet {

    private Long id;

    private String name;

    private String description;

    private Map<String, String> parameters;

    private List<Long> testScenarios;

    public DataSet(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }
}
