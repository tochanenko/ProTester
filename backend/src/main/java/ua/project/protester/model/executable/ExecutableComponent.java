package ua.project.protester.model.executable;

import lombok.Getter;
import lombok.Setter;
import org.openqa.selenium.WebDriver;
import ua.project.protester.model.executable.result.AbstractActionResult;

import java.util.Map;
import java.util.function.Consumer;

@Setter
@Getter
public abstract class ExecutableComponent {
    protected Integer id;
    protected String name;
    protected String description;
    protected ExecutableComponentType type;
    protected String[] parameterNames;

    public abstract void execute(Map<String, String> params, WebDriver driver, Consumer<AbstractActionResult> callback);
}
