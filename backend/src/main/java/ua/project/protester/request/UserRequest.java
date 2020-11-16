package ua.project.protester.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {
    private String email;

    private String password;

    private String name;

    private String fullName;

}
