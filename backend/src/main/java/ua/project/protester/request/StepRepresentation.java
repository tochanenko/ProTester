package ua.project.protester.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class StepRepresentation {
    private int id;
    private boolean isAction;
    private Map<String, String> parameters;
}
