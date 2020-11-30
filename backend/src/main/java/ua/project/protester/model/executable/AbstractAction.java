package ua.project.protester.model.executable;

import lombok.Getter;
import lombok.ToString;
import org.openqa.selenium.WebDriver;
import ua.project.protester.model.ActionType;

import java.util.Map;

@Getter
@ToString
public abstract class AbstractAction extends ExecutableComponent {

    protected Integer id;
    protected String className;
    protected Map<String, String> preparedParams;

    public void init(Integer id, String name, ActionType type, String description, String className, String[] parameterNames) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.description = description;
        this.className = className;
        this.parameterNames = parameterNames;
    }

    @Override
    public abstract void execute(Map<String, String> params, WebDriver driver);

    public void execute(WebDriver driver) {
        execute(preparedParams, driver);
    }

    public void prepare(Map<String, String> preparedParams) {
        this.preparedParams = preparedParams;
    }

    public boolean isPrepared() {
        return preparedParams != null && !preparedParams.isEmpty();
    }
}
