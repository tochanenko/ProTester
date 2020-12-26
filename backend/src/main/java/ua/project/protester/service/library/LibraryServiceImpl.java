package ua.project.protester.service.library;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.project.protester.exception.LibraryAlreadyExistsException;
import ua.project.protester.exception.LibraryNotFoundException;
import ua.project.protester.model.Library;
import ua.project.protester.model.executable.Step;
import ua.project.protester.repository.library.LibraryRepository;
import ua.project.protester.request.LibraryRequestModel;
import ua.project.protester.utils.Page;
import ua.project.protester.utils.PaginationLibrary;

import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class LibraryServiceImpl implements LibraryService {
    private final LibraryRepository libraryRepository;

    @Override
    @Transactional
    public void createLibrary(LibraryRequestModel libraryRequest) throws LibraryAlreadyExistsException {

        Library newLibrary = new Library();
        newLibrary.setName(libraryRequest.getName());
        newLibrary.setDescription(libraryRequest.getDescription());
        newLibrary.setComponents(
                libraryRequest.getComponents()
                        .stream()
                        .map(componentRepresentation -> new Step(
                                componentRepresentation.getId(),
                                componentRepresentation.isAction(),
                                null,
                                componentRepresentation.getParameters()

                        )).collect(Collectors.toList())
        );

        try {
            libraryRepository.createLibrary(newLibrary);
        } catch (Exception e) {
            throw new LibraryAlreadyExistsException("Library already exists", e);
        }

    }

    @Override
    @Transactional
    public void updateLibrary(LibraryRequestModel libraryRequest, int id) throws LibraryAlreadyExistsException {

        Library updateLibrary = new Library();
        updateLibrary.setName(libraryRequest.getName());
        updateLibrary.setDescription(libraryRequest.getDescription());
        updateLibrary.setComponents(
                libraryRequest.getComponents()
                        .stream()
                        .map(componentRepresentation -> new Step(
                                componentRepresentation.getId(),
                                componentRepresentation.isAction(),
                                null,
                                componentRepresentation.getParameters()

                        )).collect(Collectors.toList())
        );

        try {
            libraryRepository.updateLibrary(updateLibrary, id);
        } catch (Exception e) {
            throw new LibraryAlreadyExistsException("Library already exists", e);
        }
    }

    @Override
    @Transactional
    public Page<Library> findAll(PaginationLibrary paginationLibrary) {
        return new Page<>(
                libraryRepository.findAll(paginationLibrary),
                libraryRepository.getCountLibraries(paginationLibrary)
        );
    }

    @Override
    @Transactional
    public Library getLibraryById(int id) throws LibraryNotFoundException {
        return libraryRepository.findLibraryById(id).orElseThrow(() -> new LibraryNotFoundException("Can`t find library by id=" + id));
    }

    @Override
    @Transactional
    public void deleteLibraryById(int id) throws LibraryNotFoundException {
        try {
            libraryRepository.deleteLibraryById(id);
        } catch (Exception e) {
            throw new LibraryNotFoundException("Can`t find library with id=" + id);
        }
    }
}
