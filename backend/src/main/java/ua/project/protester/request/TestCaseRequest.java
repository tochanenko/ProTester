package ua.project.protester.request;

import com.fasterxml.jackson.annotation.JsonFormat;
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

    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    private List<Long> dataSetId = new ArrayList<>();
}
