package ua.project.protester.repository;

import ua.project.protester.exception.LibraryNotFoundException;
import ua.project.protester.model.Library;

import java.util.List;
import java.util.Optional;

public interface LibraryRepository {
    void createLibrary(Library library);

    void updateLibrary(Library library, int id);

    List<Library> getList(int count, int offset);

    Optional<Library> findLibraryById(Integer id) throws LibraryNotFoundException;

    Optional<Library> findByName(String name);

    void deleteLibraryById(Integer id);
}
