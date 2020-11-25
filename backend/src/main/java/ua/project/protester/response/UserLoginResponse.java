package ua.project.protester.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class UserLoginResponse {
    private String token;

    private String role;
}
