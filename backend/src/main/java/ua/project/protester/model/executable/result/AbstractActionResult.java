package ua.project.protester.model.executable.result;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.OffsetDateTime;

@Getter
@Setter
@ToString
public abstract class AbstractActionResult {
    protected Integer id;
    protected OffsetDateTime startDate;
    protected OffsetDateTime endDate;
    protected boolean result;
}
