package ua.project.protester.model.executable.result.subtype;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ActionResultRest extends ActionResultSubtype {
    private String request;
    private String response;
    private Integer statusCode;

    public ActionResultRest(Integer actionResultId, String request, String response, Integer statusCode) {
        super(actionResultId);
        this.request = request;
        this.response = response;
        this.statusCode = statusCode;
    }
}
