package ua.project.protester.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ua.project.protester.response.TestCaseResponse;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class RunTestCaseRequest {

    private Long id;

    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    private List<TestCaseResponse> testCaseResponseList;

    private Long userId;

}
