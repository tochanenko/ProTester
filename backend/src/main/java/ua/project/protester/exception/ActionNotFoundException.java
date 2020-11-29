package ua.project.protester.exception;

public class ActionNotFoundException extends RuntimeException {

    public ActionNotFoundException() {
    }

    public ActionNotFoundException(String message) {
        super(message);
    }
}
