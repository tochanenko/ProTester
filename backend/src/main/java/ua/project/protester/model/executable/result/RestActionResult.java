package ua.project.protester.model.executable.result;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import ua.project.protester.model.executable.AbstractAction;

@Getter
@Setter
public class RestActionResult extends AbstractActionResult{
    private String restInfo;
}
