package ua.project.protester.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ua.project.protester.exception.executable.compound.CompoundNotFoundException;
import ua.project.protester.exception.executable.compound.InnerCompoundDeleteException;
import ua.project.protester.model.executable.OuterComponent;
import ua.project.protester.request.OuterComponentFilter;
import ua.project.protester.request.OuterComponentRepresentation;
import ua.project.protester.service.CompoundService;
import ua.project.protester.utils.Page;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/compounds")
public class CompoundController {

    private final CompoundService compoundService;

    @PostMapping
    public OuterComponent createCompound(@RequestBody OuterComponentRepresentation request) {
        return compoundService.saveCompound(request);
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

    @DeleteMapping("/{id}")
    public OuterComponent deleteCompound(@PathVariable int id) throws InnerCompoundDeleteException {
        return compoundService.deleteCompoundById(id);
    }
}
