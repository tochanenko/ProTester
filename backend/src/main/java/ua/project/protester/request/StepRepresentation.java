package ua.project.protester.request;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class StepRepresentation {
    private int id;
    private boolean isAction;
    private Map<String, String> parameters;
}
