package ua.project.protester.model.executable;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.Map;

@RequiredArgsConstructor
@Getter
@ToString
public class Step {
    private final Integer id;
    private final boolean isAction;
    private final ExecutableComponent component;
    private final Map<String, String> parameters;
}
