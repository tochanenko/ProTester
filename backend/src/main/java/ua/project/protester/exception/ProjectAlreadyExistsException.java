package ua.project.protester.exception;

public class ProjectAlreadyExistsException extends Exception {
    public ProjectAlreadyExistsException() {
        super();
    }

    public ProjectAlreadyExistsException(String message) {
        super(message);
    }
}
