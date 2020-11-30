package ua.project.protester.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class UserLoginResponse {
    private Long id;

    private String username;

    private String email;

    private String token;

    private String role;
}
