package ua.project.protester.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ua.project.protester.exception.CompoundNotFoundException;
import ua.project.protester.exception.InnerCompoundDeleteException;
import ua.project.protester.model.executable.Compound;
import ua.project.protester.request.CreateCompoundRequest;
import ua.project.protester.service.CompoundService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/compounds")
public class CompoundController {

    private final CompoundService compoundService;

    @PostMapping
    public void createCompound(@RequestBody CreateCompoundRequest request) {
        compoundService.saveCompound(request);
    }

    @GetMapping
    public List<Compound> getAllCompounds() {
        return compoundService.getAllCompounds();
    }

    @GetMapping("/{id}")
    public Compound getCompound(@PathVariable int id) throws CompoundNotFoundException {
        return compoundService.getCompoundById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteCompound(@PathVariable int id) throws InnerCompoundDeleteException {
        compoundService.deleteCompoundById(id);
    }
}
