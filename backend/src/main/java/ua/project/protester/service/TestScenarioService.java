package ua.project.protester.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.project.protester.exception.executable.OuterComponentNotFoundException;
import ua.project.protester.exception.executable.OuterComponentStepSaveException;
import ua.project.protester.exception.executable.TestScenarioNotFoundException;
import ua.project.protester.model.executable.OuterComponent;
import ua.project.protester.repository.OuterComponentRepository;
import ua.project.protester.request.OuterComponentFilter;
import ua.project.protester.request.OuterComponentRepresentation;
import ua.project.protester.utils.Page;

@Service
@RequiredArgsConstructor
public class TestScenarioService {
    private final OuterComponentRepository outerComponentRepository;

    @Transactional
    public OuterComponent saveTestScenario(OuterComponentRepresentation outerComponentRepresentation) throws OuterComponentStepSaveException {
        OuterComponent newOuterComponent = outerComponentRepresentation.getOuterComponent();
        return outerComponentRepository.saveOuterComponent(newOuterComponent, false).orElse(null);
    }

    @Transactional
    public OuterComponent updateTestScenario(int id, OuterComponentRepresentation testScenarioRepresentation) throws OuterComponentStepSaveException {
        OuterComponent updatedTestScenario = testScenarioRepresentation.getOuterComponent();
        return outerComponentRepository.updateOuterComponent(id, updatedTestScenario, false).orElse(null);
    }

    @Transactional
    public Page<OuterComponent> getAllTestScenarios(OuterComponentFilter filter, boolean loadSteps) {
        return new Page<>(
                outerComponentRepository.findAllOuterComponents(false, filter, loadSteps),
                outerComponentRepository.countOuterComponents(false, filter));
    }

    @Transactional
    public OuterComponent getTestScenarioById(int id) throws TestScenarioNotFoundException {
        try {
            return outerComponentRepository.findOuterComponentById(id, false);
        } catch (OuterComponentNotFoundException e) {
            throw new TestScenarioNotFoundException(e);
        }
    }

    @Transactional
    public OuterComponent deleteTestScenarioById(int id) {
        return outerComponentRepository.deleteOuterComponentById(id, false).orElse(null);
    }
}
