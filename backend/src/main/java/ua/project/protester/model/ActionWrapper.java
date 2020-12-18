package ua.project.protester.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.project.protester.model.executable.result.ResultStatus;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActionWrapper {

    private Integer id;

    private String description;

    private String className;

    private String name;

    protected OffsetDateTime startDate;

    protected OffsetDateTime endDate;

    protected String message;

    private Map<String, String> parameters = new HashMap<>();

    private ResultStatus resultStatus;
}
