package ua.project.protester.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ua.project.protester.model.ActionType;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class ActionRequestModel {

    private Integer declarationId;

    private Map<String, String> preparedParams;

    private ActionType actionType;

    private String description;

    private String name;
}