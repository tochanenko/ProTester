package ua.project.protester.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import ua.project.protester.constants.MailConstants;
import ua.project.protester.exception.MailSendException;
import ua.project.protester.model.User;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;

    public void sendRegistrationCredentials(User user) throws MailSendException {
        sendMessage(user,
                MailConstants.REGISTRATION_MAIL_SUBJECT,
                MailConstants.REGISTRATION_MAIL_TEMPLATE,
                Map.of(
                        "user", user));
    }

    public void sendResetPasswordLinkMail(User user, String passwordResetLink) throws MailSendException {
        sendMessage(user,
                MailConstants.RESET_PASSWORD_LINK_MAIL_SUBJECT,
                MailConstants.RESET_PASSWORD_LINK_MAIL_TEMPLATE,
                Map.of(
                        "user", user,
                        "link", passwordResetLink));
    }

    public void sendPasswordUpdateMail(User user) throws MailSendException {
        sendMessage(user,
                MailConstants.PASSWORD_UPDATE_MAIL_SUBJECT,
                MailConstants.PASSWORD_UPDATE_MAIL_TEMPLATE,
                Map.of(
                        "user", user));
    }

    private void sendMessage(User user, String subject, String template, Map<String, Object> params) throws MailSendException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        Context context = new Context();
        params.forEach(context::setVariable);
        String text = templateEngine.process(template, context);

        try {
            helper.setTo(user.getEmail());
            helper.setSubject(subject);
            helper.setText(text, true);

            javaMailSender.send(message);
        } catch (MessagingException e) {
            throw new MailSendException("Failed to send mail!", e);
        }
    }
}
