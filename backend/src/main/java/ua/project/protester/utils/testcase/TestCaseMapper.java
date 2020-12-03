package ua.project.protester.utils.testcase;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ua.project.protester.model.TestCase;
import ua.project.protester.request.TestCaseRequest;
import ua.project.protester.response.TestCaseResponse;

@Component
@RequiredArgsConstructor
public class TestCaseMapper {

    private final ModelMapper modelMapper;

    public TestCase toEntity(TestCaseRequest testCaseRequest) {
        return modelMapper.map(testCaseRequest, TestCase.class);
    }

    public TestCaseResponse toResponse(TestCase testCase) {
        return modelMapper.map(testCase, TestCaseResponse.class);
    }
}
