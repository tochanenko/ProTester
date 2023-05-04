package ua.project.protester.exception.executable.action;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class ActionNotFoundException extends Exception {
    public ActionNotFoundException(Throwable cause) {
        super(cause);
    }

    public ActionNotFoundException(String parameterKey, Object parameterValue) {
        super("Failed to find action with " + parameterKey + " = " + parameterValue);
    }
}
