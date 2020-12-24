package ua.project.protester.exception.executable.compound;

import ua.project.protester.response.LightOuterComponentResponse;

import java.util.List;

public class InnerCompoundDeleteException extends InnerCompoundException {

    public InnerCompoundDeleteException(String message, List<LightOuterComponentResponse> outerComponents) {
        super(message, outerComponents);
    }
}
