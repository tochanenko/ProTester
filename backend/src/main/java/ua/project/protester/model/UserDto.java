package ua.project.protester.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.project.protester.annotation.NotExistingEmail;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDto {

    @NotExistingEmail(message = "user already exist")
    private String email;

    @NotNull(message = "provide a password")
    private String password;

    @NotNull (message = "provide a role")
    private Role role;
}
