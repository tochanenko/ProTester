package ua.project.protester.exception;

public class LibraryNotFoundException extends Exception {
    
    public LibraryNotFoundException(int id) {
        super(String.format("Library with id: %d not found", id));
    }
}
