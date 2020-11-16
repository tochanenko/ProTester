package ua.project.protester.exception;

/**
 * An MailSendException is thrown when mail send fails
 */
public class MailSendException extends Exception {

    public MailSendException(String message, Throwable cause) {
        super(message, cause);
    }
}
