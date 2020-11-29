package ua.project.protester.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.project.protester.model.Role;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserModificationDto {

    private Long id;

    private String email;

    private String password;

    private String username;

    private String firstName;

    private String lastName;

    private boolean isActive;

    private Role role;

}
