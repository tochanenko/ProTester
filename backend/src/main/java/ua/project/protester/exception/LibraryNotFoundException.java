package ua.project.protester.exception;

public class LibraryNotFoundException extends Exception{
    public LibraryNotFoundException() {
        super();
    }

    public LibraryNotFoundException(String message) {
        super(message);
    }
}
