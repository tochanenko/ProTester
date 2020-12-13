package ua.project.protester.model.executable.result.subtype;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SqlColumnCell {
    Integer id;
    Integer sqlColumnId;
    Integer orderNumber;
    String value;
}
