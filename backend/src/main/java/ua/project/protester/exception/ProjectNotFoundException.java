package ua.project.protester.exception;

public class ProjectNotFoundException extends Exception {
    public ProjectNotFoundException() {
        super();
    }

    public ProjectNotFoundException(String message) {
        super(message);
    }
}
