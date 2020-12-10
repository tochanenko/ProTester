package ua.project.protester.model.executable;

import lombok.Getter;
import lombok.ToString;
import org.openqa.selenium.WebDriver;
import ua.project.protester.exception.executable.action.ActionExecutionException;
import ua.project.protester.model.executable.result.ActionResult;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.function.Consumer;

@Getter
@ToString
public abstract class AbstractAction extends ExecutableComponent {

    protected String className;

    public void init(Integer id, String name, ExecutableComponentType type, String description, String className, String[] parameterNames) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.description = description;
        this.className = className;
        this.parameterNames = parameterNames;
    }

    public void execute(Map<String, String> params, WebDriver driver, Consumer<ActionResult> callback) {
        ActionResult result = new ActionResult();
        result.setStartDate(OffsetDateTime.now());

        try {
            logic(params, driver, result);
            result.setEndDate(OffsetDateTime.now());
            result.setResult(true);
        } catch (ActionExecutionException e) {
            result.setEndDate(OffsetDateTime.now());
            result.setResult(false);
        }

        callback.accept(result);
    }

    protected abstract void logic(Map<String, String> params, WebDriver driver, ActionResult result) throws ActionExecutionException;
}
