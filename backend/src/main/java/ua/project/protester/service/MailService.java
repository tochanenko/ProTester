package ua.project.protester.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import ua.project.protester.exception.MailSendException;
import ua.project.protester.request.UserCreationRequestDto;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import static ua.project.protester.constants.MailConstants.REGISTRATION_MAIL_SUBJECT;
import static ua.project.protester.constants.MailConstants.REGISTRATION_MAIL_TEMPLATE;


@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;

    public void sendRegistrationCredentials(UserCreationRequestDto userDto) throws MailSendException {
        Context context = new Context();
        context.setVariable("user", userDto);
        String text = templateEngine.process(REGISTRATION_MAIL_TEMPLATE, context);

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        try {
            helper.setTo(userDto.getEmail());
            helper.setSubject(REGISTRATION_MAIL_SUBJECT);
            helper.setText(text, true);

            javaMailSender.send(message);
        } catch (MessagingException e) {
            throw new MailSendException("Failed to send mail!", e);
        }
    }
}
