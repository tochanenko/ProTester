package ua.project.protester.constants;

import org.springframework.stereotype.Component;

@Component
public final class MailConstants {

    private MailConstants() {
    }

    public static final String REGISTRATION_MAIL_TEMPLATE = "mail/registration-mail";

    public static final String REGISTRATION_MAIL_SUBJECT = "ProTester registration";
}
