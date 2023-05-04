package ua.project.protester.utils.testcase;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ua.project.protester.model.TestCase;
import ua.project.protester.model.TestCaseDto;

@Component
@RequiredArgsConstructor
public class TestCaseMapper {

    private final ModelMapper modelMapper;

    public TestCase toEntity(TestCaseDto testCaseDto) {
        return modelMapper.map(testCaseDto, TestCase.class);
    }

    public TestCaseDto toResponse(TestCase testCase) {
        return modelMapper.map(testCase, TestCaseDto.class);
    }
}
