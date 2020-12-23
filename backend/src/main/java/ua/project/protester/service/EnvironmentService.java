package ua.project.protester.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.project.protester.model.Environment;
import ua.project.protester.repository.EnvironmentRepository;
import ua.project.protester.utils.Page;
import ua.project.protester.utils.Pagination;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EnvironmentService {

    private final EnvironmentRepository repository;

    @Transactional
    public Optional<Environment> findById(Long id) {

        return repository.findEnvironmentById(id);
    }

    @Transactional
    public Environment save(Environment environment) {

        return repository.saveEnvironment(environment);
    }

    @Transactional
    public Environment update(Environment environment) {

        return repository.updateEnvironment(environment);
    }

    @Transactional
    public List<Environment> findAll(Long projectId) {

        return repository.findAll(projectId);
    }

    @Transactional
    public void delete(Long id) {

        repository.deleteEnvironmentById(id);
    }

    @Transactional
    public Page<Environment> findAllPaginatedByProjectId(Pagination pagination, Long projectId) {
        return new Page<>(
                repository.findAll(pagination, projectId),
                repository.count(pagination, projectId)
        );
    }
}
