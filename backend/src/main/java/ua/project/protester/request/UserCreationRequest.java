package ua.project.protester.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ua.project.protester.model.UserDto;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserCreationRequest extends UserDto {
    private String username;

    private String fullName;
}
