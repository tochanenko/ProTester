package ua.project.protester.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * A MailSendException is thrown when mail send fails
 */
@ResponseStatus(code = HttpStatus.BAD_GATEWAY)
public class MailSendException extends Exception {

    public MailSendException(String message, Throwable cause) {
        super(message, cause);
    }
}
