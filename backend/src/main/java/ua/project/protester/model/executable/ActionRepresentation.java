package ua.project.protester.model.executable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ActionRepresentation {
    private Integer id;
    private String className;
    private String description;
}
