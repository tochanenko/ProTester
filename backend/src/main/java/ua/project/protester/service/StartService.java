package ua.project.protester.service;

import com.google.gson.Gson;
import jdk.jshell.Snippet;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.project.protester.exception.executable.TestScenarioNotFoundException;
import ua.project.protester.exception.executable.action.ActionExecutionException;
import ua.project.protester.exception.executable.action.IllegalActionLogicImplementation;
import ua.project.protester.model.DataSet;
import ua.project.protester.model.RunResult;
import ua.project.protester.request.RunTestCaseRequest;
import ua.project.protester.model.TestCase;
import ua.project.protester.model.executable.OuterComponent;
import ua.project.protester.model.executable.Step;
import ua.project.protester.model.executable.result.ActionResultDto;
import ua.project.protester.model.executable.result.ResultStatus;
import ua.project.protester.model.executable.result.TestCaseResultDto;
import ua.project.protester.repository.DataSetRepository;
import ua.project.protester.repository.result.ActionResultRepository;
import ua.project.protester.repository.result.RunResultRepository;
import ua.project.protester.repository.result.TestCaseResultRepository;
import ua.project.protester.request.TestCaseRequest;

import java.time.OffsetDateTime;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Service
@Slf4j
public class StartService {

     private WebDriver webDriver;
     private DataSetRepository dataSetRepository;
     private TestScenarioService testScenarioService;
     private ModelMapper modelMapper;
     private ActionResultRepository actionResultRepository;
     private TestCaseResultRepository resultRepository;
     private UserService userService;
     private RunResultRepository runResultRepository;
     private SimpMessagingTemplate messagingTemplate;

     private List<TestCaseRequest> testCaseRequest = new ArrayList<>();

     @Autowired
    public StartService(@Lazy WebDriver webDriver, DataSetRepository dataSetRepository, TestScenarioService testScenarioService, ModelMapper modelMapper, ActionResultRepository actionResultRepository, TestCaseResultRepository resultRepository, UserService userService, RunResultRepository runResultRepository, SimpMessagingTemplate messagingTemplate) {
        this.webDriver = webDriver;
        this.dataSetRepository = dataSetRepository;
        this.testScenarioService = testScenarioService;
        this.modelMapper = modelMapper;
        this.actionResultRepository = actionResultRepository;
        this.resultRepository = resultRepository;
        this.userService = userService;
        this.runResultRepository = runResultRepository;
        this.messagingTemplate = messagingTemplate;
    }

    public void execute(Long id) {

         RunResult runResult = runResultRepository.findRunResultById(id).get();

         List<Integer> testCaseResults = runResult.getTestCaseResult();
        for (int i = 0; i < this.testCaseRequest.size(); i++) {
            runTestCase(testCaseRequest.get(i), testCaseResults.get(i));
        }
        log.info("testCaseRequest are {}", this.testCaseRequest);
        this.testCaseRequest.clear();
    }

    @Transactional
    void runTestCase(TestCaseRequest testCaseRequest, int testCaseResultId) {

        Map<String, String> initMap = new HashMap<>();
        initMap.put("username", "volodya");
        initMap.put("password", "tank85943221");
        initMap.put("url", "www.youtube.com");
        initMap.put("rztk_id", "rztk_id from inout param");

        TestCase testCase = fromTestCaseRequestToModel(testCaseRequest);
        testCase.getDataSetList().stream()
                    .map(DataSet::getId)
                    .map(id -> connectDataSetWithTestScenario(testCase.getScenarioId().intValue(), id, initMap))
                    .filter(Objects::nonNull)
                    .forEachOrdered(outerComponent -> {
                                try {
                                    outerComponent.get().execute(initMap, webDriver, getConsumer(testCaseResultId));
                                    resultRepository.updateStatusAndEndDate(testCaseResultId, ResultStatus.PASSED, OffsetDateTime.now());
                                } catch (IllegalActionLogicImplementation | ActionExecutionException a) {
                                    resultRepository.updateStatusAndEndDate(testCaseResultId, ResultStatus.FAILED, OffsetDateTime.now());
                                }
                        }
                    );
    }

    @Transactional
    public RunResult getTestCaseExecutionResult(RunTestCaseRequest testCaseRequest) {

        RunResult runResult = new RunResult();
        runResult.setUserId(testCaseRequest.getUserId());
        runResult.setTestCaseResult(testCaseRequest.getTestCaseRequestList().stream()
                .map(request -> {
                    TestCaseResultDto testCaseResult = new TestCaseResultDto(userService.findUserById(request.getAuthorId()).get(), fromTestCaseRequestToModel(request));
                    List<ActionResultDto> actionResult = new ArrayList<>();
                    testCaseResult.setStatus(ResultStatus.IN_PROGRESS);
                    testCaseResult.setStartDate(OffsetDateTime.now());
                    testCaseResult.setInnerResults(actionResult);
                    return resultRepository.save(testCaseResult).getId();
                })
                .collect(Collectors.toList()));
        this.testCaseRequest = testCaseRequest.getTestCaseRequestList();
        return runResultRepository.saveUserRunResult(runResult);
    }

    @Transactional
    Consumer<ActionResultDto> getConsumer(Integer testCaseResultId) {
        return (action) -> {
            try {
                ActionResultDto actionResultDto = new ActionResultDto(action);
                actionResultDto.setStatus(ResultStatus.IN_PROGRESS);
                messagingTemplate.convertAndSend("/topic/public/" + testCaseResultId, actionResultDto);
                actionResultDto = actionResultRepository.save(testCaseResultId, action);
                messagingTemplate.convertAndSend("/topic/public/" + testCaseResultId, actionResultDto);
            } catch (IllegalActionLogicImplementation illegalActionLogicImplementation) {
                illegalActionLogicImplementation.printStackTrace();
            }
        };
    }

    @Transactional
    Optional<OuterComponent> connectDataSetWithTestScenario(Integer scenarioId, Long dataSetId, Map<String, String> initMap) {
        try {
            OuterComponent testScenario = testScenarioService.getTestScenarioById(scenarioId);
            DataSet dataSet = dataSetRepository.findDataSetById(dataSetId).get();
            List<Step> stepsParams = testScenario.getSteps();
            for (Step s : stepsParams
            ) {
                for (Map.Entry<String, String> entry : s.getParameters().entrySet()) {
                    if (dataSet.getDataset().containsKey(entry.getValue()) && !initMap.containsValue(entry.getValue())) {
                        entry.setValue(dataSetRepository.findValueByKeyAndId(dataSetId, entry.getValue()).get());
                    }
                }
            }
            return Optional.of(testScenario);
        } catch (TestScenarioNotFoundException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Transactional
    TestCase fromTestCaseRequestToModel(TestCaseRequest testCaseRequest) {
        TestCase testCase = modelMapper.map(testCaseRequest, TestCase.class);
        List<DataSet> dataSets = new ArrayList<>();

        testCaseRequest.getDataSetId()
                .forEach(id -> dataSets.add(dataSetRepository.findDataSetById(id).get()));
        testCase.setDataSetList(dataSets);
        return testCase;
    }

    @Transactional
    public RunResult findById(Long id) {
         return runResultRepository.findRunResultById(id).orElseThrow(RuntimeException::new);
    }
}
