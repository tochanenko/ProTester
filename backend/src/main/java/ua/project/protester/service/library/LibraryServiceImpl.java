package ua.project.protester.service.library;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    public void createLibrary(LibraryRequestModel libraryRequest) {
        System.out.println(libraryRequest.toString());
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
        libraryRepository.createLibrary(newLibrary);
    }

    @Override
    @Transactional
    public void updateLibrary(LibraryRequestModel libraryRequest, int id) {
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
        libraryRepository.updateLibrary(updateLibrary, id);
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
        try {
            return libraryRepository.findLibraryById(id).orElseThrow(LibraryNotFoundException::new);
        } catch (LibraryNotFoundException e) {
            throw new LibraryNotFoundException();
        }
    }

    @Override
    @Transactional
    public void deleteLibraryById(int id) {
        libraryRepository.deleteLibraryById(id);
    }
}
