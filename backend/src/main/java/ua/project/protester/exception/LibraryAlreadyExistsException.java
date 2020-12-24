package ua.project.protester.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class LibraryAlreadyExistsException extends Exception {
    public LibraryAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
