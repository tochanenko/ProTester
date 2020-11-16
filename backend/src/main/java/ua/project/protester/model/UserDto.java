package ua.project.protester.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDto {

    private Long id;

    private String name;

    private String password;

    private String email;

    private boolean isActive;

    private String fullName;

    private Role roles;

}
