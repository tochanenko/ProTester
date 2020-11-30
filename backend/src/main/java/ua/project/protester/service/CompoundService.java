package ua.project.protester.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.project.protester.exception.CompoundNotFoundException;
import ua.project.protester.exception.InnerCompoundDeleteException;
import ua.project.protester.model.executable.Step;
import ua.project.protester.model.executable.Compound;
import ua.project.protester.repository.CompoundRepository;
import ua.project.protester.request.CreateCompoundRequest;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompoundService {

    private final CompoundRepository compoundRepository;

    @Transactional
    public void saveCompound(CreateCompoundRequest compoundRequest) {
        Compound newCompound = new Compound();
        newCompound.setName(compoundRequest.getName());
        newCompound.setDescription(compoundRequest.getDescription());
        newCompound.setSteps(
                compoundRequest.getSteps()
                        .stream()
                        .map(stepRepresentation -> new Step(
                                stepRepresentation.getId(),
                                stepRepresentation.isAction(),
                                null,
                                stepRepresentation.getParameters()))
                        .collect(Collectors.toList()));
        compoundRepository.saveCompound(newCompound);
    }

    public List<Compound> getAllCompounds() {
        return compoundRepository.findAll();
    }

    public Compound getCompoundById(int id) throws CompoundNotFoundException {
        return compoundRepository.findCompoundById(id)
                .orElseThrow(CompoundNotFoundException::new);
    }

    @Transactional
    public void deleteCompoundById(int id) throws InnerCompoundDeleteException {
        if (compoundRepository.compoundWithIdIsInnerComponent(id)) {
            throw new InnerCompoundDeleteException("Failed to delete compound with id");
        }
        compoundRepository.deleteCompoundById(id);
    }
}
