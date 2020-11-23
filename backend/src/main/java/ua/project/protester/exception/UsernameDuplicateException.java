package ua.project.protester.exception;

public class UsernameDuplicateException extends RuntimeException {
    public UsernameDuplicateException(String username) {
        super(String.format("User with username  %s already exist", username));
    }
}
