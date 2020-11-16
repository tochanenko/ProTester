package ua.project.protester.exceptions;

public class UserServiceException extends Exception {

    private String message;

    public UserServiceException(String message, String message1) {
        super(message);
        this.message = message1;
    }

    public UserServiceException() {
    }
}
