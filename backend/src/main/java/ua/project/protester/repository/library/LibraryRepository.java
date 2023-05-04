package ua.project.protester.repository.library;

import ua.project.protester.exception.LibraryNotFoundException;
import ua.project.protester.model.Library;
import ua.project.protester.utils.PaginationLibrary;

import java.util.List;
import java.util.Optional;

public interface LibraryRepository {
    void createLibrary(Library library);

    void updateLibrary(Library library, int id);

    List<Library> findAll(PaginationLibrary paginationLibrary);

    Optional<Library> findLibraryById(Integer id) throws LibraryNotFoundException;

    void deleteLibraryById(Integer id) throws LibraryNotFoundException;

    Long getCountLibraries(PaginationLibrary paginationLibrary);
}
