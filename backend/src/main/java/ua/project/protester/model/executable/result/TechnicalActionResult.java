package ua.project.protester.model.executable.result;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ua.project.protester.model.executable.AbstractAction;

@Getter
@Setter
public class TechnicalActionResult extends AbstractActionResult {
    private String technicalInfo;
}
