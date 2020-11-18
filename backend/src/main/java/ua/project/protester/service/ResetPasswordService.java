package ua.project.protester.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.project.protester.model.UserDto;

@Service
@RequiredArgsConstructor
public class ResetPasswordService {

    private final MailService mailService;

    public void processResetPasswordRequest(UserDto user) {
        // Check if user exists in db

        // EXISTS
        // Create token and save it
        String tokenValue = "";
        // Create reset link
        String passwordResetLink = "localhost:8080/api/forgot-password/confirm-reset?t="
                + tokenValue;
        // Create the email (link with token inside) and send it
        mailService.sendResetPasswordLinkMail(user, passwordResetLink);

        // NOT EXISTS
        // throw exception
    }

    public String processTokenValidation(String tokenValue) {
        // Check if token exists

        // EXISTS
        // Get user email from token
        // Return user email
        return "user@email.com";

        // NOT EXISTS
        // throw exception
    }

    public void processPasswordReset(UserDto user) {
        // Check if user email is not null

        // IF NOT NULL
        // Find user by email
        // Update password
        // Save user
        // Send the email
        mailService.sendPasswordUpdateMail(user);

        // IF NULL
        // throw exception
    }
}
