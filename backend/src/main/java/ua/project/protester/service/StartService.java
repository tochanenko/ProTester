package ua.project.protester.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.project.protester.exception.executable.OuterComponentNotFoundException;
import ua.project.protester.model.DataSet;
import ua.project.protester.model.TestCase;
import ua.project.protester.model.executable.OuterComponent;
import ua.project.protester.repository.DataSetRepository;
import ua.project.protester.repository.OuterComponentRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StartService {

    private final OuterComponentRepository outerComponentRepository;
    private final DataSetRepository dataSetRepository;

    public void runTestCase(TestCase testCase) {

    }

    private void connectDataSetWithTestScenario(TestCase testCase) {
        try {
            OuterComponent testScenario = outerComponentRepository.findOuterComponentById(testCase.getScenarioId().intValue(), false).get();
            List<DataSet> dataSetList = dataSetRepository.findDataSetByTestCaseId(testCase.getScenarioId());

        } catch (OuterComponentNotFoundException e) {
            e.printStackTrace();
        }
    }
}
