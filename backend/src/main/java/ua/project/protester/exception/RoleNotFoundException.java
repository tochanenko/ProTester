package ua.project.protester.exception;

import lombok.Data;

@Data
public class RoleNotFoundException extends RuntimeException {
    private String message;

    public RoleNotFoundException(String message) {
        super(message);
    }
}
