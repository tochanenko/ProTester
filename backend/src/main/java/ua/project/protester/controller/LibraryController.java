package ua.project.protester.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ua.project.protester.exception.LibraryNotFoundException;
import ua.project.protester.model.Library;
import ua.project.protester.request.LibraryRequestModel;
import ua.project.protester.service.LibraryService;

import java.util.List;

@PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER', 'ENGINEER')")
@RestController
@RequestMapping("/api/library")
@RequiredArgsConstructor
public class LibraryController {
    private final LibraryService libraryService;

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public void createLibrary(@RequestBody LibraryRequestModel request) {
        libraryService.createLibrary(request);
    }

    @PutMapping("/update")
    public void updateLibrary(@RequestBody LibraryRequestModel request, @RequestParam int id) {
        libraryService.updateLibrary(request, id);
    }

    @DeleteMapping("/{id}")
    public void deleteLibrary(@PathVariable int id) {
        libraryService.deleteLibraryById(id);
    }

    @GetMapping
    public List<Library> findAll() {
        return libraryService.getAllLibraries();
    }

    @GetMapping("/{id}")
    public Library getLibraryId(@PathVariable int id) throws LibraryNotFoundException {
        return libraryService.getLibraryById(id);
    }

    @GetMapping("/name")
    public Library getLibraryName(@RequestParam String name) throws LibraryNotFoundException{
        return libraryService.getLibraryByName(name);
    }
}
