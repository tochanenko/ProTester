package ua.project.protester.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ua.project.protester.exception.executable.OuterComponentStepSaveException;
import ua.project.protester.exception.executable.compound.CompoundNotFoundException;
import ua.project.protester.exception.executable.compound.InnerCompoundDeleteException;
import ua.project.protester.exception.executable.compound.InnerCompoundEditException;
import ua.project.protester.model.executable.OuterComponent;
import ua.project.protester.request.OuterComponentFilter;
import ua.project.protester.request.OuterComponentRepresentation;
import ua.project.protester.service.CompoundService;
import ua.project.protester.utils.Page;

@PreAuthorize("isAuthenticated()")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/compounds")
public class CompoundController {

    private final CompoundService compoundService;

    @PostMapping
    public OuterComponent createCompound(@RequestBody OuterComponentRepresentation request) throws OuterComponentStepSaveException {
        return compoundService.saveCompound(request);
    }

    @PutMapping("/{id}")
    public OuterComponent updateCompound(@RequestBody OuterComponentRepresentation request, @PathVariable int id) throws OuterComponentStepSaveException, InnerCompoundEditException {
        return compoundService.updateCompound(id, request);
    }

    @GetMapping
    public Page<OuterComponent> getAllCompounds(@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                                @RequestParam(value = "pageNumber", defaultValue = "1") Integer pageNumber,
                                                @RequestParam(value = "compoundName", defaultValue = "") String compoundName,
                                                @RequestParam(value = "loadSteps", defaultValue = "true") boolean loadSteps) {
        OuterComponentFilter filter = new OuterComponentFilter(pageSize, pageNumber, compoundName);
        return compoundService.getAllCompounds(filter, loadSteps);
    }

    @GetMapping("/{id}")
    public OuterComponent getCompound(@PathVariable int id) throws CompoundNotFoundException {
        return compoundService.getCompoundById(id);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    @DeleteMapping("/{id}")
    public OuterComponent deleteCompound(@PathVariable int id) throws InnerCompoundDeleteException {
        return compoundService.deleteCompoundById(id);
    }
}
