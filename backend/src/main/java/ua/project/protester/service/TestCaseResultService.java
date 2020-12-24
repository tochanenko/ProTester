package ua.project.protester.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.project.protester.exception.ProjectNotFoundException;
import ua.project.protester.exception.result.TestCaseResultNotFoundException;
import ua.project.protester.model.executable.result.TestCaseResultDto;
import ua.project.protester.repository.result.TestCaseResultRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TestCaseResultService {

    private final TestCaseResultRepository testCaseResultRepository;

    @Transactional
    public TestCaseResultDto getTestCaseResultById(int id) throws TestCaseResultNotFoundException {
        return testCaseResultRepository.findById(id);
    }

    public List<TestCaseResultDto> findAllByProjectId(Long projectId) throws ProjectNotFoundException {
        return testCaseResultRepository.findAllByProjectId(projectId);
    }
}
