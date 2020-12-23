package ua.project.protester.exception.executable.compound;

import ua.project.protester.response.LightOuterComponentResponse;

import java.util.List;

public class InnerCompoundEditException extends InnerCompoundException {

    public InnerCompoundEditException(String message, List<LightOuterComponentResponse> outerComponents) {
        super(message, outerComponents);
    }
}
