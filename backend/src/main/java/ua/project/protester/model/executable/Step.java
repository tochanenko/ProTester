package ua.project.protester.model.executable;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor
@Getter
public class Step {
    private final Integer id;
    private final boolean isAction;
    private final ExecutableComponent component;
    private final Map<String, String> parameters;
}
