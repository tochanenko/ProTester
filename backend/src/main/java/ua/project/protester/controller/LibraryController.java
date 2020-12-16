package ua.project.protester.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ua.project.protester.exception.LibraryNotFoundException;
import ua.project.protester.model.Library;
import ua.project.protester.request.LibraryRequestModel;
import ua.project.protester.service.LibraryService;
import ua.project.protester.utils.Page;
import ua.project.protester.utils.PaginationLibrary;

@Slf4j
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
    public Page<Library> getAll(@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                @RequestParam(value = "pageNumber", defaultValue = "1") Integer pageNumber,
                                @RequestParam(value = "libraryName", defaultValue = "") String libraryName) {

        PaginationLibrary pagination = new PaginationLibrary(pageSize, pageNumber, libraryName);
        log.info("pagination:", pagination);
        return libraryService.findAll(pagination);
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
