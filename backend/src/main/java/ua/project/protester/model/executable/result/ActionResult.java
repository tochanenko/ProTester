package ua.project.protester.model.executable.result;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ActionResult {
    private Integer id;
    private Integer testCaseResultId;
    private Integer actionId;
    private OffsetDateTime startDate;
    private OffsetDateTime endDate;
    private Integer statusId;
    private String message;
}
