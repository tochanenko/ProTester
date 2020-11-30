package ua.project.protester.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ua.project.protester.model.executable.ExecutableComponentType;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class ActionRequestModel {

    private Integer id;

    private String description;

    private String name;

    private ExecutableComponentType type;

    private Map<String, String> preparedParams;

}
