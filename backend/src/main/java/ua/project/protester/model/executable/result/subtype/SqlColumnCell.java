package ua.project.protester.model.executable.result.subtype;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SqlColumnCell {
    private Integer id;
    private Integer sqlColumnId;
    private Integer orderNumber;
    private String value;
}
