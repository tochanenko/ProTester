package ua.project.protester.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.project.protester.exception.executable.OuterComponentStepSaveException;
import ua.project.protester.exception.executable.compound.CompoundNotFoundException;
import ua.project.protester.exception.executable.compound.InnerCompoundDeleteException;
import ua.project.protester.exception.executable.OuterComponentNotFoundException;
import ua.project.protester.model.executable.OuterComponent;
import ua.project.protester.repository.OuterComponentRepository;
import ua.project.protester.request.OuterComponentFilter;
import ua.project.protester.request.OuterComponentRepresentation;
import ua.project.protester.utils.Page;

@Service
@RequiredArgsConstructor
public class CompoundService {

    private final OuterComponentRepository outerComponentRepository;

    @Transactional
    public OuterComponent saveCompound(OuterComponentRepresentation compoundRequest) throws OuterComponentStepSaveException {
        OuterComponent newOuterComponent = compoundRequest.getOuterComponent();
        return outerComponentRepository.saveOuterComponent(newOuterComponent, true).orElse(null);
    }

    @Transactional
    public Page<OuterComponent> getAllCompounds(OuterComponentFilter filter, boolean loadSteps) {
        return new Page<>(
                outerComponentRepository.findAllOuterComponents(true, filter, loadSteps),
                outerComponentRepository.countOuterComponents(true, filter));
    }

    @Transactional
    public OuterComponent getCompoundById(int id) throws CompoundNotFoundException {
        try {
            return outerComponentRepository.findOuterComponentById(id, true);
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
