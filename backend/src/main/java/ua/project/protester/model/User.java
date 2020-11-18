package ua.project.protester.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class User {

    private Long id;

    private String name;

    private String password;

    private String email;

    private boolean isActive;

    private String fullName;

    private Role role;

}
