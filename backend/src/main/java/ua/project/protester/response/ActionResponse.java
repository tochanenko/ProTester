package ua.project.protester.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActionResponse {

    private Integer id;

    private String name;

    private String type;

    private String description;

    private Map<String, String> preparedParams;
}
