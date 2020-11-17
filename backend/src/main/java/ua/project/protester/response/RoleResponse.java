package ua.project.protester.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleResponse  {

    private Long id;

    private String name;

    private List<UserResponse> users;

}
