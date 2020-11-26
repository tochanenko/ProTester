package ua.project.protester.model;

import lombok.*;
import ua.project.protester.annotation.Action;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@ToString
public class ActionDeclaration {
    private Integer id;
    private String className;
    private ActionType type;
    private String defaultDescription;

    public void loadMetadata() throws ClassNotFoundException {
        Action metadata = Class.forName(className).getAnnotation(Action.class);
        this.type = metadata.type();
        this.defaultDescription = metadata.defaultDescription();
    }
}
