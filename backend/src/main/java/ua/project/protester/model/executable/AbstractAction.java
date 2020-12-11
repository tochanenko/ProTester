package ua.project.protester.model.executable;

import lombok.Getter;
import lombok.ToString;
import org.openqa.selenium.WebDriver;
import ua.project.protester.exception.executable.action.ActionExecutionException;
import ua.project.protester.model.executable.result.ActionResult;
import ua.project.protester.model.executable.result.ResultStatus;

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

    @Override
    public void execute(Map<String, String> params, WebDriver driver, Consumer<ActionResult> callback) throws ActionExecutionException {
        ActionResult result = new ActionResult();
        result.setActionName(name);
        result.setType(type);
        result.setStartDate(OffsetDateTime.now());
        try {
            logic(params, driver, result);
            result.setEndDate(OffsetDateTime.now());
            result.setStatus(ResultStatus.PASSED);
        } catch (Exception e) {
            result.setEndDate(OffsetDateTime.now());
            result.setStatus(ResultStatus.FAILED);
            result.setMessage(e.getMessage());
            throw new ActionExecutionException(e.getMessage(), e);
        } finally {
            callback.accept(result);
        }
    }

    protected abstract void logic(Map<String, String> params, WebDriver driver, ActionResult result) throws Exception;
}
