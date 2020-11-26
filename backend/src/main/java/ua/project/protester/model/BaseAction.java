package ua.project.protester.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.openqa.selenium.WebDriver;

import java.util.Map;

@Getter
@Setter
@ToString
public abstract class BaseAction {

    protected Integer id;
    protected Integer declarationId;
    protected String description;
    protected String[] parameterNames;
    protected Map<String, String> preparedParams;

    public void init(String description, String[] parameterNames) {
        this.description = description;
        this.parameterNames = parameterNames;
    }

    public boolean hasSameSignature(BaseAction that) {
        return this.declarationId.equals(that.declarationId);
    }

    public void prepare(Map<String, String> params) {
        preparedParams = params;
    }

    public abstract void invoke(Map<String, String> params, WebDriver driver);

    public void invoke(WebDriver driver) {
        invoke(preparedParams, driver);
    }
}
