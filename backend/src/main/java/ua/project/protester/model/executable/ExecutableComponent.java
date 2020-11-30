package ua.project.protester.model.executable;

import lombok.Getter;
import org.openqa.selenium.WebDriver;
import ua.project.protester.model.ActionType;

import java.util.Map;

@Getter
public abstract class ExecutableComponent {
    protected String name;
    protected ActionType type;
    protected String description;
    protected String[] parameterNames;

    public abstract void execute(Map<String, String> params, WebDriver driver);
}
