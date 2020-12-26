package ua.project.protester.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ua.project.protester.model.TestCaseDto;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class RunTestCaseRequest {

    private Long id;

    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    private List<TestCaseDto> testCaseResponseList;

    private Long userId;

}
