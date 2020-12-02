package ua.project.protester.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ua.project.protester.exception.executable.compound.CompoundNotFoundException;
import ua.project.protester.exception.executable.compound.InnerCompoundDeleteException;
import ua.project.protester.model.executable.OuterComponent;
import ua.project.protester.request.OuterComponentRepresentation;
import ua.project.protester.service.CompoundService;

import java.util.List;

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
    public List<OuterComponent> getAllCompounds() {
        return compoundService.getAllCompounds();
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
