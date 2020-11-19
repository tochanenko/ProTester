package ua.project.protester.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@NoArgsConstructor
@Data
public class PasswordResetToken {

    /**
     * Token expiration time in millisecond
     */
    private static final int EXPIRATION = 1000 * 60 * 10;

    private String value;
    private long userId;
    private Date expiryDate;

    public PasswordResetToken(long userId) {
        this.userId = userId;
        this.value = UUID.randomUUID().toString();
        this.expiryDate = new Date(System.currentTimeMillis() + EXPIRATION);
    }
}
