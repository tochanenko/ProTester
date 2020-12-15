package ua.project.protester.exception.result;

public class ResultSubtypeNotFoundException extends Exception {
    public ResultSubtypeNotFoundException(Integer id) {
        this(id, null);
    }

    public ResultSubtypeNotFoundException(Integer id, Throwable cause) {
        super("Failed to load subtype for result with id =" + id, cause);
    }
}
