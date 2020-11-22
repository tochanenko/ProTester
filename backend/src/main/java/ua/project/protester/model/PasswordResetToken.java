package ua.project.protester.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

@NoArgsConstructor
@Data
public class PasswordResetToken {

    /**
     * Token expiration time in minutes
     */
    private static final int EXPIRATION = 10;

    private String value;
    private long userId;
    private OffsetDateTime expiryDate;

    public PasswordResetToken(long userId) {
        this.userId = userId;
        this.value = UUID.randomUUID().toString();
        this.expiryDate = OffsetDateTime.now().plusMinutes(EXPIRATION);
    }
}
