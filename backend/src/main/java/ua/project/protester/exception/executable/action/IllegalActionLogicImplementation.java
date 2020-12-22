package ua.project.protester.exception.executable.action;

import java.util.List;

public class IllegalActionLogicImplementation extends Exception {
    public IllegalActionLogicImplementation(String message) {
        super(message);
    }

    public IllegalActionLogicImplementation(List<String> invalidActionClasses) {
        super(String.format(
                "There is something wrong with this actions: %s. Please, check log for more details",
                invalidActionClasses));
    }
}
