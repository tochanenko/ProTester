package ua.project.protester.exception.result;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class TestCaseResultNotFoundException extends Exception {
    public TestCaseResultNotFoundException(Integer id, Throwable cause) {
        super("Failed to find test case result with id=" + id, cause);
    }

    public TestCaseResultNotFoundException(Integer id) {
        super("Failed to find test case result with id=" + id);
    }
}
