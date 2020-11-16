package ua.project.protester.model;

import lombok.*;

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

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", isActive=" + isActive +
                ", fullName='" + fullName + '\'' +
                ", roles=" + role +
                '}';
    }
}
