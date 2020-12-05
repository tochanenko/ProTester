package ua.project.protester.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ua.project.protester.exception.executable.compound.CompoundNotFoundException;
import ua.project.protester.exception.executable.compound.InnerCompoundDeleteException;
import ua.project.protester.model.executable.OuterComponent;
import ua.project.protester.request.BaseFilter;
import ua.project.protester.request.OuterComponentRepresentation;
import ua.project.protester.service.CompoundService;
import ua.project.protester.utils.Page;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/compounds")
public class CompoundController {

    private final CompoundService compoundService;

    @PostMapping
    public void createCompound(@RequestBody OuterComponentRepresentation request) {
        compoundService.saveCompound(request);
    }

    @GetMapping
    public Page<OuterComponent> getAllCompounds(@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                                @RequestParam(value = "pageNumber", defaultValue = "1") Integer pageNumber,
                                                @RequestParam(value = "compoundName", defaultValue = "") String compoundName) {
        BaseFilter filter = new BaseFilter(pageSize, pageNumber, compoundName);
        return compoundService.getAllCompounds(filter);
    }

    @GetMapping("/{id}")
    public OuterComponent getCompound(@PathVariable int id) throws CompoundNotFoundException {
        return compoundService.getCompoundById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteCompound(@PathVariable int id) throws InnerCompoundDeleteException {
        compoundService.deleteCompoundById(id);
    }
}
