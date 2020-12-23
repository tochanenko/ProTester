package ua.project.protester.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
@Getter
public class EnvironmentNotFoundException extends RuntimeException {

    private Long environmentId;

    public EnvironmentNotFoundException(String message) {
        super(message);
    }

    public EnvironmentNotFoundException(String message, Long environmentId) {
        super(message);
        this.environmentId = environmentId;
    }
}
