package ua.project.protester.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.ToString;
import org.openqa.selenium.WebDriver;

import java.util.Map;

@Getter
@ToString
public abstract class BaseAction {

    protected Integer id;
    @Getter(AccessLevel.NONE)
    protected ActionDeclaration actionDeclaration;
    protected String description;
    protected String[] parameterNames;
    protected Map<String, String> preparedParams;

    public Integer getDeclarationId() {
        return actionDeclaration.getId();
    }

    public String getName() {
        return actionDeclaration.getClassName();
    }

    public ActionType getType() {
        return actionDeclaration.getType();
    }

    public String getDescription() {
        if (this.description == null || this.description.isEmpty()) {
            return actionDeclaration.getDefaultDescription();
        }
        return description;
    }

    public boolean hasDefaultDescription() {
        return description == null || description.isEmpty();
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void init(ActionDeclaration actionDeclaration, String[] parameterNames) {
        this.actionDeclaration = actionDeclaration;
        this.parameterNames = parameterNames;
    }

    public void init(Integer id,
                     ActionDeclaration actionDeclaration,
                     String description,
                     String[] parameterNames,
                     Map<String, String> preparedParams) {
        this.id = id;
        this.description = description;
        init(actionDeclaration, parameterNames);
        prepare(preparedParams);
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
