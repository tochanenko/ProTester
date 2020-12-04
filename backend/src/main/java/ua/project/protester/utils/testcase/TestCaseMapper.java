package ua.project.protester.utils.testcase;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ua.project.protester.model.TestCase;
import ua.project.protester.request.TestCaseRequest;
import ua.project.protester.response.DataSetResponse;
import ua.project.protester.response.TestCaseResponse;
import ua.project.protester.utils.DataSetMapper;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TestCaseMapper {

    private final ModelMapper modelMapper;
    private final DataSetMapper dataSetMapper;

    public TestCase toEntity(TestCaseRequest testCaseRequest) {
        return modelMapper.map(testCaseRequest, TestCase.class);
    }

    public TestCaseResponse toResponse(TestCase testCase) {
        TestCaseResponse testCaseResponse = modelMapper.map(testCase, TestCaseResponse.class);
        List<DataSetResponse> dataSetResponses = testCase.getDataSetList()
                .stream()
                .map(dataSetMapper::toDataSetResponseFromDataSet)
                .collect(Collectors.toList());
        testCaseResponse.setDataSetResponseList(dataSetResponses);
        return testCaseResponse;
    }
}
