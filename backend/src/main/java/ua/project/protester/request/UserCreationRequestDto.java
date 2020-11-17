package ua.project.protester.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ua.project.protester.model.UserDto;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserCreationRequestDto extends UserDto {
    @NotNull(message = "provide a username")
    private String username;

    private String fullName;
}
