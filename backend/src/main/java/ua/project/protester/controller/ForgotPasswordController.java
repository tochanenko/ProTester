package ua.project.protester.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.project.protester.model.UserDto;
import ua.project.protester.service.ResetPasswordService;

@RestController
@RequestMapping("/api/forgot-password")
@RequiredArgsConstructor
public class ForgotPasswordController {

    private final ResetPasswordService resetPasswordService;

    @PostMapping
    public ResponseEntity<?> resetPasswordRequest(@RequestBody UserDto user) {
        resetPasswordService.processResetPasswordRequest(user);
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/confirm-reset")
    public ResponseEntity<String> validateResetToken(@RequestBody String tokenValue) {
        String email = resetPasswordService.processTokenValidation(tokenValue);
        return ResponseEntity.ok(email);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody UserDto user) {
        resetPasswordService.processPasswordReset(user);
        return ResponseEntity.ok().build();
    }
}
