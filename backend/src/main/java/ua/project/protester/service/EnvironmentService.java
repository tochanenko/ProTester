package ua.project.protester.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.project.protester.model.Environment;
import ua.project.protester.repository.EnvironmentRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EnvironmentService {

    private final EnvironmentRepository repository;

    public Optional<Environment> findById(Long id) {
        return repository.findEnvironmentById(id);
    }

    public Environment save(Environment environment) {
        return repository.saveEnvironment(environment);
    }

    public Environment update(Environment environment) {return repository.updateEnvironment(environment);}

    public List<Environment> findAll() {return repository.findAll();}

    public void delete(Long id) {repository.deleteEnvironmentById(id);}
}
