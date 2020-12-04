package ua.project.protester.service;

import org.springframework.transaction.annotation.Transactional;
import ua.project.protester.exception.LibraryNotFoundException;
import ua.project.protester.model.Library;
import ua.project.protester.request.LibraryRequestModel;
import ua.project.protester.utils.Page;
import ua.project.protester.utils.PaginationLibrary;


public interface LibraryService {
    @Transactional
    void createLibrary(LibraryRequestModel libraryRequest);

    @Transactional
    void updateLibrary(LibraryRequestModel libraryRequest, int id);

    @Transactional
    Page<Library> findAll(PaginationLibrary paginationLibrary);

    @Transactional
    Library findLibraryByName(String name) throws LibraryNotFoundException;

    @Transactional
    Library getLibraryById(int id) throws LibraryNotFoundException;

    @Transactional
    void deleteLibraryById(int id);
}
