package ua.project.protester.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.project.protester.annotation.UniqueEmail;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDto {

    @UniqueEmail(message = "User with given email already exist")
    @NotNull
    private String email;

    @NotNull(message = "provide a password")
    private String password;

}
