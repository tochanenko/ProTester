package ua.project.protester.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.project.protester.model.Environment;
import ua.project.protester.repository.EnvironmentRepository;

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
}
