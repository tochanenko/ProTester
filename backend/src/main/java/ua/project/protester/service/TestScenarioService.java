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
    public OuterComponent saveTestScenario(OuterComponentRepresentation outerComponentRepresentation) {
        OuterComponent newOuterComponent = constructOuterComponentFromRepresentation(outerComponentRepresentation);
        return outerComponentRepository.saveOuterComponent(newOuterComponent, false).orElse(null);
    }

    @Transactional
    public OuterComponent updateTestScenario(int id, OuterComponentRepresentation testScenarioRepresentation) {
        OuterComponent updatedTestScenario = constructOuterComponentFromRepresentation(testScenarioRepresentation);
        return outerComponentRepository.updateTestScenario(id, updatedTestScenario).orElse(null);
    }

    public Page<OuterComponent> getAllTestScenarios(OuterComponentFilter filter, boolean loadSteps) {
        return new Page<>(
                outerComponentRepository.findAllOuterComponents(false, filter, loadSteps),
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
    public OuterComponent deleteTestScenarioById(int id) {
        return outerComponentRepository.deleteOuterComponentById(id, false).orElse(null);
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
