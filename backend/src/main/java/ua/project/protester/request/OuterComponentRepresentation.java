package ua.project.protester.request;

import lombok.Getter;
import lombok.Setter;
import ua.project.protester.model.executable.OuterComponent;
import ua.project.protester.model.executable.Step;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class OuterComponentRepresentation {

    private String name;
    private String description;
    private List<StepRepresentation> steps;

    public OuterComponent getOuterComponent() {
        OuterComponent newOuterComponent = new OuterComponent();
        newOuterComponent.setName(name);
        newOuterComponent.setDescription(description);
        newOuterComponent.setSteps(steps
                .stream()
                .map(stepRepresentation -> new Step(
                        stepRepresentation.getId(),
                        stepRepresentation.isAction(),
                        null,
                        stepRepresentation.getParameters()))
                .collect(Collectors.toList()));
        return newOuterComponent;
    }
}
