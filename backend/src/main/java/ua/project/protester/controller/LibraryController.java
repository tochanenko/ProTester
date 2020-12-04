package ua.project.protester.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ua.project.protester.exception.LibraryNotFoundException;
import ua.project.protester.model.Library;
import ua.project.protester.request.LibraryRequestModel;
import ua.project.protester.service.LibraryService;

import java.util.List;

@RestController
@RequestMapping("/api/library")
@RequiredArgsConstructor
public class LibraryController {

    private final LibraryService libraryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createLibrary(@RequestBody LibraryRequestModel request) {
        libraryService.createLibrary(request);
    }

    @PutMapping("/{id}")
    public void updateLibrary(@RequestBody LibraryRequestModel request, @PathVariable int id) {
        libraryService.updateLibrary(request, id);
    }

    @DeleteMapping("/{id}")
    public void deleteLibrary(@PathVariable int id) {
        libraryService.deleteLibraryById(id);
    }

    @GetMapping
    public List<Library> getList(@RequestParam int count,
                                 @RequestParam int offset) {
        return libraryService.getList(count, offset);
    }

    @GetMapping("/{id}")
    public Library getLibraryById(@PathVariable int id) throws LibraryNotFoundException {
        return libraryService.getLibraryById(id);
    }

    @GetMapping("/name")
    public Library findLibraryByName(@RequestParam String name) throws LibraryNotFoundException {
        return libraryService.findLibraryByName(name);
    }
}
