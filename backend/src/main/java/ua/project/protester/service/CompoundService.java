package ua.project.protester.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.project.protester.exception.executable.compound.CompoundNotFoundException;
import ua.project.protester.exception.executable.compound.InnerCompoundDeleteException;
import ua.project.protester.exception.executable.OuterComponentNotFoundException;
import ua.project.protester.model.executable.Step;
import ua.project.protester.model.executable.OuterComponent;
import ua.project.protester.repository.OuterComponentRepository;
import ua.project.protester.request.OuterComponentFilter;
import ua.project.protester.request.OuterComponentRepresentation;
import ua.project.protester.utils.Page;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompoundService {

    private final OuterComponentRepository outerComponentRepository;

    @Transactional
    public OuterComponent saveCompound(OuterComponentRepresentation compoundRequest) {
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
        return outerComponentRepository.saveOuterComponent(newOuterComponent, true).orElse(null);
    }

    public Page<OuterComponent> getAllCompounds(OuterComponentFilter filter, boolean loadSteps) {
        return new Page<>(
                outerComponentRepository.findAllOuterComponents(true, filter, loadSteps),
                outerComponentRepository.countOuterComponents(true, filter));
    }

    public OuterComponent getCompoundById(int id) throws CompoundNotFoundException {
        try {
            return outerComponentRepository.findOuterComponentById(id, true)
                    .orElseThrow(OuterComponentNotFoundException::new);
        } catch (OuterComponentNotFoundException e) {
            throw new CompoundNotFoundException(e);
        }
    }

    @Transactional
    public OuterComponent deleteCompoundById(int id) throws InnerCompoundDeleteException {
        if (outerComponentRepository.compoundWithIdIsInnerComponent(id)) {
            throw new InnerCompoundDeleteException("Attempt to delete inner compound");
        }
        return outerComponentRepository.deleteOuterComponentById(id, true).orElse(null);
    }
}
