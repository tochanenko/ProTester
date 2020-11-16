package ua.project.protester.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import ua.project.protester.exception.MailSendException;
import ua.project.protester.model.UserDto;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;


@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;

    @SuppressWarnings("unused")
    public void sendRegistrationCredentials(UserDto userDto) throws MailSendException {
        Context context = new Context();
        context.setVariable("user", userDto);
        String text = templateEngine.process("mail/registration-mail", context);

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        try {
            helper.setTo(userDto.getEmail());
            helper.setSubject("ProTester registration");
            helper.setText(text, true);

            javaMailSender.send(message);
        } catch (MessagingException e) {
            throw new MailSendException("Failed to send mail!", e);
        }
    }
}
