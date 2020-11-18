package ua.project.protester.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.project.protester.annotation.UniqueUsername;
import ua.project.protester.model.Role;
import ua.project.protester.model.UserDto;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserModificationDto extends UserDto {

    private Long id;

    @UniqueUsername
    private String name;

    private String fullName;

    @NotNull(message = "give a status to user")
    private boolean isActive;

    @NotNull(message = "provide a role")
    private Role role;

}
