package ua.project.protester.exception.executable;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class TestScenarioNotFoundException extends Exception {

    public TestScenarioNotFoundException() {
        super();
    }

    public TestScenarioNotFoundException(Throwable cause) {
        super(cause);
    }
}
