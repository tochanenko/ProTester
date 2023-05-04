package ua.project.protester.exception.result;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class RunResultNotFoundException extends RuntimeException {
    public RunResultNotFoundException(String message) {
        super(message);
    }
}
