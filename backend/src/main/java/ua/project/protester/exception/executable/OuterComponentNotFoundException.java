package ua.project.protester.exception.executable;

public class OuterComponentNotFoundException extends RuntimeException {

    private static final String MESSAGE = "Failed to find outer component (id=%d, isCompound=%s)";

    public OuterComponentNotFoundException(int id, boolean isCompound) {
        super(String.format(MESSAGE, id, isCompound));
    }

    public OuterComponentNotFoundException(int id, boolean isCompound, Throwable throwable) {
        super(String.format(MESSAGE, id, isCompound), throwable);
    }
}
