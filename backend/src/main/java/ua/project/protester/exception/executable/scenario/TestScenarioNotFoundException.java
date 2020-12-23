package ua.project.protester.exception.executable.scenario;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class TestScenarioNotFoundException extends Exception {
    public TestScenarioNotFoundException(Throwable cause) {
        super(cause);
    }
}
