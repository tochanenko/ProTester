package ua.project.protester.model.executable;

import lombok.Getter;
import lombok.Setter;
import okhttp3.OkHttpClient;
import org.openqa.selenium.WebDriver;
import ua.project.protester.exception.executable.action.ActionExecutionException;
import ua.project.protester.exception.executable.action.IllegalActionLogicImplementation;
import ua.project.protester.model.Environment;
import ua.project.protester.model.executable.result.ActionResultDto;

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

    public abstract void execute(Map<String, String> params, Map<String, String> context, Environment environment, WebDriver driver, OkHttpClient okHttpClient, Consumer<ActionResultDto> callback) throws ActionExecutionException, IllegalActionLogicImplementation;
}
