package ua.project.protester.model.executable.result.subtype;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SqlColumnDto {
    private String name;
    private List<String> rows;
}
