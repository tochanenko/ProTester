package ua.project.protester.request;

import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
public class TestCaseResultFilter {
    private Integer pageSize;
    private Integer pageNumber;
    private Integer userId;
    private Integer testCaseId;
    private String status;
    private OffsetDateTime dateTimeFrom;
    private OffsetDateTime dateTimeTo;

    public TestCaseResultFilter() {
        pageSize = 10;
        pageNumber = 1;
    }

    public Integer getOffset() {
        return (pageNumber - 1) * pageSize;
    }
}
