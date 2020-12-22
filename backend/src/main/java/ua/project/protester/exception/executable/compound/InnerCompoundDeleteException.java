package ua.project.protester.exception.executable.compound;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import ua.project.protester.response.LightOuterComponentResponse;

import java.util.List;

@Getter
@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class InnerCompoundDeleteException extends Exception {

    private final List<LightOuterComponentResponse> outerComponents;

    public InnerCompoundDeleteException(String message, List<LightOuterComponentResponse> outerComponents) {
        super(message);
        this.outerComponents = outerComponents;
    }
}
