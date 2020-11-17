package ua.project.protester.service;

import org.springframework.stereotype.Service;

@Service
public class ResetPasswordService {

    // Pass User
    public void processResetPasswordRequest() {
        // Check if user exists in db

        // EXISTS
        // Create token and save it
        // Save it
        // Create the email (link with token inside)
        // Send the email

        // NOT EXISTS
        // throw exception
    }

    // Pass token
    public void processTokenValidation() {
        // Check if token exists

        // EXISTS
        // Get user email from token
        // Return user email

        // NOT EXISTS
        // throw exception
    }

    // Pass User
    public void processPasswordReset() {
        // Check if user email is not null

        // IF NOT NULL
        // Find user by email
        // Update password
        // Save user

        // IF NULL
        // throw exception
    }
}
