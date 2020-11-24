package ua.project.protester.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.openqa.selenium.WebDriver;

import java.util.Map;

@Getter
@Setter
@ToString
public class BaseAction {

    protected Integer id;
    protected ActionType type;
    protected String name;
    protected String description;
    protected String[] parameterNames;

    public void init(ActionType type, String name, String description, String[] parameterNames) {
        this.id = null;
        this.type = type;
        this.name = name;
        this.description = description;
        this.parameterNames = parameterNames;
    }

    public boolean hasSameSignature(BaseAction that) {
        return this.name.equals(that.name);
    }

    public void invoke(Map<String, String> params, WebDriver driver) {
    }
}
