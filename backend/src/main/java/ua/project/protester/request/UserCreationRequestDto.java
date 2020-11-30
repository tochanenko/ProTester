package ua.project.protester.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ua.project.protester.annotation.UniqueUsername;
import ua.project.protester.model.Role;
import ua.project.protester.model.UserDto;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserCreationRequestDto extends UserDto {


    @NotNull(message = "provide a firstname")
    @UniqueUsername(message = "username already exists")
    private String firstName;

    @NotNull (message = "provide a lastname")
    private String lastName;

    @UniqueUsername
    @NotNull(message = "provide a username")
    private String username;

    @NotNull (message = "provide a role")
    private Role role;

    @NotNull(message = "provide a status")
    private boolean isActive;
}
