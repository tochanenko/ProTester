package ua.project.protester.exception.executable.compound;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class CompoundNotFoundException extends Exception {
    public CompoundNotFoundException(Throwable cause) {
        super(cause);
    }
}
