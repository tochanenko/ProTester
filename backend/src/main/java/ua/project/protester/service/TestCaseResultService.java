package ua.project.protester.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.project.protester.exception.result.TestCaseResultNotFoundException;
import ua.project.protester.model.executable.result.TestCaseResultDto;
import ua.project.protester.repository.result.TestCaseResultRepository;
import ua.project.protester.utils.Page;
import ua.project.protester.utils.Pagination;

@Service
@RequiredArgsConstructor
public class TestCaseResultService {

    private final TestCaseResultRepository testCaseResultRepository;

    @Transactional
    public TestCaseResultDto getTestCaseResultById(int id) throws TestCaseResultNotFoundException {
        return testCaseResultRepository.findById(id);
    }

    @Transactional
    public Page<TestCaseResultDto> findAllResultsByProject(Pagination pagination, Long projectId) {
        return new Page<>(
                testCaseResultRepository.findAllByProjectId(pagination, projectId),
                testCaseResultRepository.countTestCaseResult(pagination, projectId)
        );
    }

    @Transactional
    public Page<TestCaseResultDto> findAllResults(Pagination pagination) {
        return new Page<>(
                testCaseResultRepository.findAllProjectsResult(pagination),
                testCaseResultRepository.countAllTestCases(pagination)
        );
    }
}
