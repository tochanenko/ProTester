package ua.project.protester.model.executable.result;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.OffsetDateTime;
import java.util.Map;

@Getter
@Setter
@ToString
public class ActionResult {
    private Integer id;
    private OffsetDateTime startDate;
    private OffsetDateTime endDate;
    private Map<String, String> extra;
    private boolean result;
}
