package ua.project.protester.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.project.protester.exception.result.TestCaseResultNotFoundException;
import ua.project.protester.model.executable.result.TestCaseResultDto;
import ua.project.protester.repository.result.TestCaseResultRepository;
import ua.project.protester.request.TestCaseResultFilter;
import ua.project.protester.utils.Page;

@Service
@RequiredArgsConstructor
public class TestCaseResultService {

    private final TestCaseResultRepository testCaseResultRepository;

    @Transactional
    public TestCaseResultDto getTestCaseResultById(int id) throws TestCaseResultNotFoundException {
        return testCaseResultRepository.findById(id);
    }

    @Transactional
    public Page<TestCaseResultDto> getAllTestCaseResults(TestCaseResultFilter filter, boolean loadActionResults) {
        return new Page<>(
                testCaseResultRepository.findAll(filter, loadActionResults),
                testCaseResultRepository.countAll(filter)
        );
    }
}
