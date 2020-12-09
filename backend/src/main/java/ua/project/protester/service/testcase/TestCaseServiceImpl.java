package ua.project.protester.service.testcase;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.project.protester.exception.TestCaseNotFoundException;
import ua.project.protester.model.TestCase;
import ua.project.protester.repository.DataSetRepository;
import ua.project.protester.repository.testCase.TestCaseRepository;
import ua.project.protester.request.TestCaseRequest;
import ua.project.protester.response.TestCaseResponse;
import ua.project.protester.utils.Page;
import ua.project.protester.utils.Pagination;
import ua.project.protester.utils.testcase.TestCaseMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class TestCaseServiceImpl implements TestCaseService {

    private final TestCaseRepository testCaseRepository;
    private final TestCaseMapper testCaseMapper;
    private final DataSetRepository dataSetRepository;

    @Transactional
    @Override
    public TestCaseResponse create(TestCaseRequest testCaseRequest) {
        log.info("IN create testCase, {}", testCaseRequest);

        TestCase testCase = testCaseMapper.toEntity(testCaseRequest);
        return testCaseMapper.toResponse(testCaseRepository.create(testCase, testCaseRequest.getDataSetId()));
    }

    @Transactional
    @Override
    public TestCaseResponse update(TestCaseRequest testCaseRequest) {
        log.info("IN update testCase, {}", testCaseRequest);

        TestCase testCase = testCaseMapper.toEntity(testCaseRequest);
        return testCaseMapper.toResponse(testCaseRepository.update(testCase, testCaseRequest.getDataSetId()));
    }

    @Transactional
    @Override
    public void delete(Long id) throws TestCaseNotFoundException {
        log.info("IN delete testCase, id={}", id);

        TestCase testCase = getTestCaseById(id);
        testCaseRepository.delete(testCase.getId());
    }

    @Override
    public TestCaseResponse findById(Long id) throws TestCaseNotFoundException {
        log.info("IN  findById, id={}", id);

        TestCase testCase = getTestCaseById(id);
        return testCaseMapper.toResponse(testCase);
    }


    @Override
    public Page<TestCaseResponse> findAllProjectTestCases(Pagination pagination, Long projectId) {
        log.info("IN findAllProjectTestCases, pagination={}, projectId={}", pagination, projectId);

        List<TestCaseResponse> testCasesMapped = testCaseRepository.findAllProjectTestCases(pagination, projectId).stream()
                .map(testCaseMapper::toResponse)
                .collect(Collectors.toList());

        return new Page<>(
                testCasesMapped,
                testCaseRepository.getCountTestCase(pagination, projectId)
        );
    }

    private TestCase getTestCaseById(Long id) throws TestCaseNotFoundException {
        return testCaseRepository.findById(id)
                .orElseThrow(TestCaseNotFoundException::new);
    }
}
