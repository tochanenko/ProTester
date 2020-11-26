package ua.project.protester.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ua.project.protester.exception.DeactivatedUserAccessException;
import ua.project.protester.exception.InvalidPasswordResetTokenException;
import ua.project.protester.exception.MailSendException;
import ua.project.protester.exception.UserNotFoundException;
import ua.project.protester.model.PasswordResetToken;
import ua.project.protester.model.User;
import ua.project.protester.repository.PasswordResetTokenRepository;
import ua.project.protester.repository.UserRepository;

import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
@PropertySource("classpath:/application.properties")
public class ResetPasswordService {

    @Value("${custom.password.reset.link}")
    private String passwordResetLink;
    @Value("${custom.password.reset.token.expiration}")
    private int tokenExpirationTime;

    private final MailService mailService;
    private final UserRepository userRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;

    public void processResetPasswordRequest(String userEmail) throws UserNotFoundException, MailSendException, DeactivatedUserAccessException {
        User user = userRepository
                .findUserByEmail(userEmail)
                .orElseThrow(UserNotFoundException::new);

        if (!user.isActive()) {
            throw new DeactivatedUserAccessException();
        }

        PasswordResetToken token = new PasswordResetToken(user.getId());
        token.setExpirationTime(tokenExpirationTime);
        tokenRepository.save(token);

        mailService.sendResetPasswordLinkMail(user, String.format(passwordResetLink, token.getValue()));
    }

    public String processTokenValidation(String tokenValue) throws InvalidPasswordResetTokenException {
        OffsetDateTime tokenExpiryDate = tokenRepository
                .findExpiryDateByValue(tokenValue)
                .orElseThrow(InvalidPasswordResetTokenException::new);

        if (tokenExpiryDate.isBefore(OffsetDateTime.now())) {
            throw new InvalidPasswordResetTokenException();
        }

        return userRepository
                .findUserEmailByTokenValue(tokenValue)
                .orElseThrow(InvalidPasswordResetTokenException::new);
    }

    public void processPasswordReset(String userEmail, String newUserPassword) throws UserNotFoundException, MailSendException, DeactivatedUserAccessException {
        User user = userRepository
                .findUserByEmail(userEmail)
                .orElseThrow(UserNotFoundException::new);

        if (!user.isActive()) {
            throw new DeactivatedUserAccessException();
        }

        userRepository.updatePassword(user, passwordEncoder.encode(newUserPassword));
        mailService.sendPasswordUpdateMail(user);
    }
}
