package ua.project.protester.exception;

import lombok.Data;

@Data
public class UserNotExistException extends RuntimeException {
    private String message;

    public UserNotExistException(String message) {
        super(message);
        this.message = message;
    }
}
