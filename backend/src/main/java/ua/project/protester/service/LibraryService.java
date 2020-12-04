package ua.project.protester.service;

import org.springframework.transaction.annotation.Transactional;
import ua.project.protester.exception.LibraryNotFoundException;
import ua.project.protester.model.Library;
import ua.project.protester.request.LibraryRequestModel;

import java.util.List;

public interface LibraryService {
    @Transactional
    void createLibrary(LibraryRequestModel libraryRequest);

    @Transactional
    void updateLibrary(LibraryRequestModel libraryRequest, int id);

    @Transactional
    List<Library> getList(int count, int offset);

    @Transactional
    Library findLibraryByName(String name) throws LibraryNotFoundException;

    @Transactional
    Library getLibraryById(int id) throws LibraryNotFoundException;

    @Transactional
    void deleteLibraryById(int id);
}
