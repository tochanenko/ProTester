package ua.project.protester.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
}
