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

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;

    public void sendRegistrationCredentials(User user) throws MailSendException {
        Context context = new Context();
        context.setVariable("user", user);
        String text = templateEngine.process(MailConstants.REGISTRATION_MAIL_TEMPLATE, context);

        sendMessage(user, MailConstants.REGISTRATION_MAIL_SUBJECT, text);
    }

    public void sendResetPasswordLinkMail(User user, String passwordResetLink) throws MailSendException {
        Context context = new Context();
        context.setVariable("user", user);
        context.setVariable("link", passwordResetLink);
        String text = templateEngine.process(MailConstants.RESET_PASSWORD_LINK_MAIL_TEMPLATE, context);

        sendMessage(user, MailConstants.RESET_PASSWORD_LINK_MAIL_SUBJECT, text);
    }

    public void sendPasswordUpdateMail(User user) throws MailSendException {
        Context context = new Context();
        context.setVariable("user", user);
        String text = templateEngine.process(MailConstants.PASSWORD_UPDATE_MAIL_TEMPLATE, context);

        sendMessage(user, MailConstants.PASSWORD_UPDATE_MAIL_SUBJECT, text);
    }

    private void sendMessage(User user, String subject, String text) throws MailSendException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
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
