package ua.project.protester.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.jdbc.DataSourceBuilder;

import javax.sql.DataSource;

@Setter
@Getter
public class Environment {
    private Long id;

    private String name;

    private String description;

    private String username;

    private String password;

    private String url;


    public Environment(Long id, String name, String description, String username, String password, String url) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.username = username;
        this.password = password;
        this.url = url;
    }

    public DataSource getDataSource(Environment environment) {
        return DataSourceBuilder
                .create()
                .url(environment.getUrl())
                .username(environment.getUsername())
                .password(environment.getPassword())
                .build();
    }
}
