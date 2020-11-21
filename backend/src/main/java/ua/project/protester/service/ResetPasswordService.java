package ua.project.protester.service;

import lombok.RequiredArgsConstructor;
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

import java.util.Date;

@Service
@RequiredArgsConstructor
public class ResetPasswordService {

    private static final String PASSWORD_RESET_LINK =
            "https://pro-tester.herokuapp.com/api/forgot-password/confirm-reset?t=%s";
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
        tokenRepository.save(token);

        mailService.sendResetPasswordLinkMail(user, String.format(PASSWORD_RESET_LINK, token.getValue()));
    }

    public String processTokenValidation(String tokenValue) throws InvalidPasswordResetTokenException {
        Date tokenExpiryDate = tokenRepository
                .findExpiryDateByValue(tokenValue)
                .orElseThrow(InvalidPasswordResetTokenException::new);

        if (tokenExpiryDate.before(new Date())) {
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
