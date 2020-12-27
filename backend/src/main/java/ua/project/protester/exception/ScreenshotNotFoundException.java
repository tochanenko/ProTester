package ua.project.protester.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class ScreenshotNotFoundException extends Exception {
    public ScreenshotNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
