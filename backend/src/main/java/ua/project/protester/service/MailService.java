package ua.project.protester.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import ua.project.protester.model.UserDto;


@Service
public class MailService {

    private static final String REGISTRATION_MAIL_BLANK =
            "Welcome to ProTester, %s%n"
                    + "%n"
                    + "Your credentials:%n"
                    + "- Login: %s%n"
                    + "- Password: %s";
    private final JavaMailSender javaMailSender;

    public MailService(final JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @SuppressWarnings("unused")
    public void sendRegistrationCredentials(UserDto userDto) {
        SimpleMailMessage mail = new SimpleMailMessage();

        mail.setTo(userDto.getEmail());
        mail.setSubject("ProTester registration");
        mail.setText(String.format(REGISTRATION_MAIL_BLANK,
                userDto.getFullName(),
                userDto.getEmail(),
                userDto.getPassword()));

        javaMailSender.send(mail);
    }
}
