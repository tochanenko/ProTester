package ua.project.protester.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.project.protester.exception.executable.OuterComponentNotFoundException;
import ua.project.protester.exception.executable.TestScenarioNotFoundException;
import ua.project.protester.model.executable.OuterComponent;
import ua.project.protester.model.executable.Step;
import ua.project.protester.repository.OuterComponentRepository;
import ua.project.protester.request.CreateOuterComponentRequest;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TestScenarioService {
    private final OuterComponentRepository outerComponentRepository;

    @Transactional
    public void saveTestScenario(CreateOuterComponentRequest compoundRequest) {
        OuterComponent newOuterComponent = new OuterComponent();
        newOuterComponent.setName(compoundRequest.getName());
        newOuterComponent.setDescription(compoundRequest.getDescription());
        newOuterComponent.setSteps(
                compoundRequest.getSteps()
                        .stream()
                        .map(stepRepresentation -> new Step(
                                stepRepresentation.getId(),
                                stepRepresentation.isAction(),
                                null,
                                stepRepresentation.getParameters()))
                        .collect(Collectors.toList()));
        outerComponentRepository.saveOuterComponent(newOuterComponent, false);
    }

    @Transactional
    public void updateTestScenario(int id, CreateOuterComponentRequest compoundRequest) throws TestScenarioNotFoundException {
        if (outerComponentRepository.existsOuterComponentWithId(id, false)) {
            outerComponentRepository.deleteOuterComponentById(id, false);
            saveTestScenario(compoundRequest);
        } else {
            throw new TestScenarioNotFoundException();
        }
    }

    public List<OuterComponent> getAllTestScenarios() {
        return outerComponentRepository.findAllOuterComponents(false);
    }

    public OuterComponent getTestScenarioById(int id) throws TestScenarioNotFoundException {
        try {
            return outerComponentRepository.findOuterComponentById(id, false)
                    .orElseThrow(OuterComponentNotFoundException::new);
        } catch (OuterComponentNotFoundException e) {
            throw new TestScenarioNotFoundException(e);
        }
    }

    @Transactional
    public void deleteTestScenarioById(int id) {
        outerComponentRepository.deleteOuterComponentById(id, false);
    }
}
