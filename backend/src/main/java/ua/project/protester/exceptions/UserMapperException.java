package ua.project.protester.exceptions;

public class UserMapperException extends Exception {
    private String message;

    public UserMapperException(String message) {
        super(message);
    }
}
