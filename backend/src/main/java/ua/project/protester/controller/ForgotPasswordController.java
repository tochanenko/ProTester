package ua.project.protester.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.project.protester.service.ResetPasswordService;

@RestController
@RequestMapping("/api/forgot-password")
@RequiredArgsConstructor
public class ForgotPasswordController {

    private final ResetPasswordService resetPasswordService;

    @PostMapping
    public String resetPasswordRequest() {
        resetPasswordService.processResetPasswordRequest();
        return "";
    }

    @GetMapping("/confirm-reset")
    public String validateResetToken() {
        resetPasswordService.processTokenValidation();
        return "";
    }

    @PostMapping("/reset-password")
    public String resetPassword() {
        resetPasswordService.processPasswordReset();
        return "";
    }
}
