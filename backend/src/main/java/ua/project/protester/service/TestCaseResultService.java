package ua.project.protester.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.project.protester.exception.result.TestCaseResultNotFoundException;
import ua.project.protester.model.executable.result.ResultStatus;
import ua.project.protester.model.executable.result.TestCaseResultDto;
import ua.project.protester.repository.result.TestCaseResultRepository;

import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class TestCaseResultService {

    private final TestCaseResultRepository testCaseResultRepository;

    @Transactional
    public TestCaseResultDto getTestCaseResultById(int id) throws TestCaseResultNotFoundException {
        return testCaseResultRepository.findById(id);
    }

    public int finishTestCaseResultWithId(int id, ResultStatus finalStatus) {
        return testCaseResultRepository.updateStatusAndEndDate(
                id,
                finalStatus,
                OffsetDateTime.now());
    }
}
