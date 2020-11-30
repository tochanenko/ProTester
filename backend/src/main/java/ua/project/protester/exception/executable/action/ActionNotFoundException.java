package ua.project.protester.exception.executable.action;

public class ActionNotFoundException extends RuntimeException {

    public ActionNotFoundException() {
    }

    public ActionNotFoundException(String message) {
        super(message);
    }
}
