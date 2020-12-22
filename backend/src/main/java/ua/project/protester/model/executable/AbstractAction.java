package ua.project.protester.model.executable;

import lombok.Getter;
import lombok.ToString;
import okhttp3.OkHttpClient;
import org.openqa.selenium.WebDriver;
import ua.project.protester.exception.executable.action.ActionExecutionException;
import ua.project.protester.exception.executable.action.IllegalActionLogicImplementation;
import ua.project.protester.model.executable.result.ActionResultDto;
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
    public void execute(Map<String, String> params, Map<String, String> context, WebDriver driver, OkHttpClient okHttpClient, Consumer<ActionResultDto> callback) throws ActionExecutionException, IllegalActionLogicImplementation {
        OffsetDateTime startDate = OffsetDateTime.now();

        ActionResultDto actionResult = logic(params, context, driver, okHttpClient);

        actionResult.setEndDate(OffsetDateTime.now());
        actionResult.setStartDate(startDate);
        actionResult.setInputParameters(params);
        actionResult.setAction(this);

        callback.accept(actionResult);

        if (actionResult.getStatus() == null) {
            throw new IllegalActionLogicImplementation("Action result status is null for action with name '" + name + "'. Please, specify status in logic implementation!");
        }
        if (actionResult.getStatus() == ResultStatus.FAILED) {
            if (actionResult.getMessage() == null) {
                throw new IllegalActionLogicImplementation("Action result status is " + ResultStatus.FAILED + ", but no exception is provided. Please, specify exception in logic implementation!");
            } else {
                throw new ActionExecutionException(actionResult.getMessage());
            }
        }
    }

    protected abstract ActionResultDto logic(Map<String, String> params, Map<String, String> context, WebDriver driver, OkHttpClient okHttpClient);
}
