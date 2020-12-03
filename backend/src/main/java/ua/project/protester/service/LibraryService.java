package ua.project.protester.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.project.protester.exception.LibraryAlreadyExistsException;
import ua.project.protester.exception.LibraryNotFoundException;
import ua.project.protester.model.Library;
import ua.project.protester.model.executable.Step;
import ua.project.protester.repository.LibraryRepository;
import ua.project.protester.request.LibraryRequestModel;

import java.util.stream.Collectors;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LibraryService {

    private final LibraryRepository libraryRepository;

    @Transactional
    public void createLibrary(LibraryRequestModel libraryRequest) {
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


    public List<Library> getAllLibraries() {
        return libraryRepository.findAllLibraries();
    }

    public Library getLibraryByName(String name) throws LibraryNotFoundException {
        return libraryRepository.findByName(name).orElseThrow(LibraryNotFoundException::new);
    }

    public Library getLibraryById(int id) throws LibraryNotFoundException {
        try {
             return libraryRepository.findLibraryById(id).orElseThrow(LibraryNotFoundException::new);
        } catch(LibraryNotFoundException e) {
            throw new LibraryNotFoundException();
        }
    }

    @Transactional
    public void deleteLibraryById(int id) {
        libraryRepository.deleteLibraryById(id);
    }
}
