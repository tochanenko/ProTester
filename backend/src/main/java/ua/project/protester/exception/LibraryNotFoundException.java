package ua.project.protester.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class LibraryNotFoundException extends Exception {
    public LibraryNotFoundException() {
        super();
    }

    public LibraryNotFoundException(String message) {
        super(message);
    }
}
