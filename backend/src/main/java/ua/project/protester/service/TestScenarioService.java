package ua.project.protester.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.project.protester.exception.executable.OuterComponentNotFoundException;
import ua.project.protester.exception.executable.TestScenarioNotFoundException;
import ua.project.protester.model.executable.OuterComponent;
import ua.project.protester.model.executable.Step;
import ua.project.protester.repository.OuterComponentRepository;
import ua.project.protester.request.OuterComponentFilter;
import ua.project.protester.request.OuterComponentRepresentation;
import ua.project.protester.utils.Page;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TestScenarioService {
    private final OuterComponentRepository outerComponentRepository;

    @Transactional
    public void saveTestScenario(OuterComponentRepresentation outerComponentRepresentation) {
        OuterComponent newOuterComponent = constructOuterComponentFromRepresentation(outerComponentRepresentation);
        outerComponentRepository.saveOuterComponent(newOuterComponent, false);
    }

    @Transactional
    public void updateTestScenario(int id, OuterComponentRepresentation testScenarioRepresentation) throws TestScenarioNotFoundException {
        if (outerComponentRepository.existsOuterComponentWithId(id, false)) {
            OuterComponent updatedTestScenario = constructOuterComponentFromRepresentation(testScenarioRepresentation);
            outerComponentRepository.updateTestScenario(id, updatedTestScenario);
        } else {
            throw new TestScenarioNotFoundException();
        }
    }

    public Page<OuterComponent> getAllTestScenarios(OuterComponentFilter filter) {
        return new Page<>(
                outerComponentRepository.findAllOuterComponents(false, filter),
                outerComponentRepository.countOuterComponents(false, filter));
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

    private OuterComponent constructOuterComponentFromRepresentation(OuterComponentRepresentation representation) {
        OuterComponent newOuterComponent = new OuterComponent();
        newOuterComponent.setName(representation.getName());
        newOuterComponent.setDescription(representation.getDescription());
        newOuterComponent.setSteps(
                representation.getSteps()
                        .stream()
                        .map(stepRepresentation -> new Step(
                                stepRepresentation.getId(),
                                stepRepresentation.isAction(),
                                null,
                                stepRepresentation.getParameters()))
                        .collect(Collectors.toList()));
        return newOuterComponent;
    }
}
