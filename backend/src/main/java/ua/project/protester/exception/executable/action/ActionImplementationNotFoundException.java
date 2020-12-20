package ua.project.protester.exception.executable.action;

public class ActionImplementationNotFoundException extends RuntimeException {
    public ActionImplementationNotFoundException(String message) {
        super(message);
    }

    public ActionImplementationNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
