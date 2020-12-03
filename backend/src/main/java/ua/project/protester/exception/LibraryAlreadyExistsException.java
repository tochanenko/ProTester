package ua.project.protester.exception;

public class LibraryAlreadyExistsException extends Exception{
    public LibraryAlreadyExistsException() {
        super();
    }

    public LibraryAlreadyExistsException(String message) {
        super(message);
    }

    public LibraryAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
