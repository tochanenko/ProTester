package ua.project.protester.model.executable.result.subtype;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ua.project.protester.exception.executable.action.ActionExecutionException;
import ua.project.protester.model.executable.result.ActionResultDto;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ActionResultTechnicalDto extends ActionResultDto {
    private Map<String, String> extra;

    public ActionResultTechnicalDto(ActionExecutionException e) {
        this(e, null);
    }

    public ActionResultTechnicalDto(ActionExecutionException e, Map<String, String> extra) {
        super(e);
        this.extra = extra;
    }
}
