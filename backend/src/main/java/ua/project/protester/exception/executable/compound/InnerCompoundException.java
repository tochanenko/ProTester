package ua.project.protester.exception.executable.compound;

import lombok.Getter;
import ua.project.protester.response.LightOuterComponentResponse;

import java.util.List;

@Getter
public abstract class InnerCompoundException extends Exception {

    protected List<LightOuterComponentResponse> outerComponents;

    public InnerCompoundException(String message, List<LightOuterComponentResponse> outerComponents) {
        super(message);
        this.outerComponents = outerComponents;
    }
}
