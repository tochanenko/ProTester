package ua.project.protester.model.executable.result.subtype;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActionResultRest extends ActionResultSubtype {
    private String request;
    private String response;
    private Integer statusCode;
}
