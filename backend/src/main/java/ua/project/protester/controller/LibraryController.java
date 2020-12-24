package ua.project.protester.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ua.project.protester.exception.LibraryNotFoundException;
import ua.project.protester.model.Library;
import ua.project.protester.request.LibraryRequestModel;
import ua.project.protester.service.library.LibraryService;
import ua.project.protester.utils.Page;
import ua.project.protester.utils.PaginationLibrary;

@PreAuthorize("isAuthenticated()")
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
    @ResponseStatus(HttpStatus.OK)
    public void updateLibrary(@RequestBody LibraryRequestModel request, @PathVariable int id) {
        libraryService.updateLibrary(request, id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteLibrary(@PathVariable int id) throws LibraryNotFoundException {
        libraryService.deleteLibraryById(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<Library> getAll(@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                @RequestParam(value = "pageNumber", defaultValue = "1") Integer pageNumber,
                                @RequestParam(value = "libraryName", defaultValue = "") String libraryName) {

        PaginationLibrary pagination = new PaginationLibrary(pageSize, pageNumber, libraryName);
        return libraryService.findAll(pagination);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Library getLibraryById(@PathVariable int id) throws LibraryNotFoundException {
        return libraryService.getLibraryById(id);
    }
}
