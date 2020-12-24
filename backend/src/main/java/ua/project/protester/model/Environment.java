package ua.project.protester.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Environment {
    private Long id;

    private String name;

    private String description;

    private String username;

    private String password;

    private String url;

    private Long projectId;

    public Environment(Long id, String name, String description, String username, String password, String url) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.username = username;
        this.password = password;
        this.url = url;
    }
}
