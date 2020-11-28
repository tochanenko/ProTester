package ua.project.protester.model;

import lombok.Getter;
import lombok.ToString;
import org.openqa.selenium.WebDriver;

import java.util.Map;

@Getter
@ToString
public abstract class BaseAction {

    protected Integer id;
    protected String name;
    protected ActionType type;
    protected String description;
    protected String className;
    protected String[] parameterNames;
    protected Map<String, String> preparedParams;

    public void init(Integer id, String name, ActionType type, String description, String className, String[] parameterNames) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.description = description;
        this.className = className;
        this.parameterNames = parameterNames;
    }

    public abstract void invoke(Map<String, String> params, WebDriver driver);

    public void invoke(WebDriver driver) {
        invoke(preparedParams, driver);
    }

    public void prepare(Map<String, String> preparedParams) {
        this.preparedParams = preparedParams;
    }

    public boolean isPrepared() {
        return preparedParams != null && !preparedParams.isEmpty();
    }
}
