package ua.project.protester.service;

import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.modelmapper.ModelMapper;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.project.protester.exception.DataSetNotFoundException;
import ua.project.protester.exception.executable.OuterComponentNotFoundException;
import ua.project.protester.exception.executable.TestScenarioNotFoundException;
import ua.project.protester.exception.executable.action.ActionExecutionException;
import ua.project.protester.exception.executable.action.IllegalActionLogicImplementation;
import ua.project.protester.exception.result.RunResultNotFoundException;
import ua.project.protester.model.*;
import ua.project.protester.model.executable.OuterComponent;
import ua.project.protester.model.executable.Step;
import ua.project.protester.model.executable.result.ActionResultDto;
import ua.project.protester.model.executable.result.ResultStatus;
import ua.project.protester.model.executable.result.TestCaseResultDto;
import ua.project.protester.repository.DataSetRepository;
import ua.project.protester.repository.result.ActionResultRepository;
import ua.project.protester.repository.result.RunResultRepository;
import ua.project.protester.repository.result.TestCaseResultRepository;
import ua.project.protester.request.RunTestCaseRequest;
import ua.project.protester.response.TestCaseResponse;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
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

    private static List<TestCaseResponse> testCaseResponses = new ArrayList<>();
    private static int counter = 0;

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

        RunResult runResult = runResultRepository.findRunResultById(id).orElseThrow();
        List<TestCaseWrapperResult> testCaseResults = runResult.getTestCaseResults();
          for (int i = 0; i < testCaseResponses.size(); i++) {
            runTestCase(testCaseResponses.get(i), testCaseResults.get(i).getTestResultId());
        }
        log.info("testCaseResponses are {}", testCaseResponses);
        testCaseResponses.clear();
    }

    @Transactional
    void runTestCase(TestCaseResponse testCaseResponse, int testCaseResultId) {

        Map<String, String> initMap = new HashMap<>();
        initMap.put("username", "volodya");
        initMap.put("password", "tank85943221");
        initMap.put("url", "www.youtube.com");
        initMap.put("rztk_id", "rztk_id from input param");

        //input text ${username} with id ${id}
        OkHttpClient okHttpClient = new OkHttpClient();
        Environment environment = new Environment();
        TestCase testCase = fromTestCaseResponseToModel(testCaseResponse);
        testCase.getDataSetList().stream()
                .map(DataSet::getId)
                .map(id -> connectDataSetWithTestScenario(testCase.getScenarioId().intValue(), id, initMap))
                .filter(Objects::nonNull)
                .forEachOrdered(outerComponent -> {
                            try {
                                outerComponent.orElseThrow(() -> new OuterComponentNotFoundException(1, false)).execute(initMap, environment, webDriver, okHttpClient, getConsumer(testCaseResultId));
                                resultRepository.updateStatusAndEndDate(testCaseResultId, ResultStatus.PASSED, OffsetDateTime.now());
                                counter = 0;
                            } catch (ActionExecutionException | OuterComponentNotFoundException | IllegalActionLogicImplementation a) {
                                resultRepository.updateStatusAndEndDate(testCaseResultId, ResultStatus.FAILED, OffsetDateTime.now());
                                counter = 0;
                            }
                        }
                );
    }

    @Transactional
    public RunResult getTestCaseExecutionResult(RunTestCaseRequest runTestCaseRequest) throws TestScenarioNotFoundException {

        RunResult resultFromDb = runResultRepository.saveRunResult(runTestCaseRequest.getUserId());

        resultFromDb.setTestCaseResults(runTestCaseRequest.getTestCaseResponseList().stream()
                .map(response -> {
                    TestCaseResultDto testCaseResult = new TestCaseResultDto(userService.findUserById(response.getAuthorId()).orElseThrow(), fromTestCaseResponseToModel(response));
                    List<ActionResultDto> actionResult = new ArrayList<>();
                    testCaseResult.setStatus(ResultStatus.IN_PROGRESS);
                    testCaseResult.setStartDate(OffsetDateTime.now());
                    testCaseResult.setInnerResults(actionResult);
                    TestCaseWrapperResult result = runResultRepository.saveTestCaseWrapperResult(response, resultFromDb.getId().intValue(), resultRepository.save(testCaseResult).getId());
                    result.setScenarioId(response.getScenarioId().intValue());
                    return result;
                })
                .collect(Collectors.toList()));

        for (int i = 0; i < runTestCaseRequest.getTestCaseResponseList().size(); i++) {
            TestCaseResponse currentTestCaseResponse = runTestCaseRequest.getTestCaseResponseList().get(i);
            TestCaseWrapperResult currentTestCaseWrapperResult = resultFromDb.getTestCaseResults().get(i);
            List<Step> step = testScenarioService.getTestScenarioById(currentTestCaseResponse.getScenarioId().intValue()).getSteps();
            List<ActionWrapper> actionWrappers = runResultRepository.saveActionWrappersByTestCaseResultWrapperId(currentTestCaseWrapperResult.getId(), step);
            resultFromDb.getTestCaseResults().get(i).setActionWrapperList(actionWrappers);
        }
        StartService.testCaseResponses = runTestCaseRequest.getTestCaseResponseList();

        return resultFromDb;
    }


    @Transactional
    Consumer<ActionResultDto> getConsumer(Integer testCaseResultId) {
        return (action) -> {
            try {
                List<ActionWrapper> actionWrappers = runResultRepository.findActionWrapperByTestCaseResult(testCaseResultId,
                        runResultRepository.findScenarioIdByTestCaseWrapperResult(testCaseResultId));
                ActionResultDto actionResultDto = actionResultRepository.save(testCaseResultId, action);
                actionResultDto.setEndDateStr(LocalDateTime.from(actionResultDto.getEndDate()).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
                actionResultDto.setStartDateStr(LocalDateTime.from(actionResultDto.getStartDate()).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
                actionWrappers.get(counter).setActionResultDtoId(actionResultDto.getId());

                if (counter == actionWrappers.size() - 1 || actionResultDto.getStatus().equals(ResultStatus.FAILED)) {
                    actionResultDto.setLast(true);
                }

                log.info("action result {}", actionResultDto.getClass().getName());
                log.info("actionWrapper {}", actionWrappers.get(counter));
                messagingTemplate.convertAndSend("/topic/public/" + actionWrappers.get(counter).getId(), actionResultDto);
                counter++;

            } catch (TestScenarioNotFoundException | IllegalActionLogicImplementation  illegalActionLogicImplementation) {
                illegalActionLogicImplementation.printStackTrace();
            }
        };
    }


    @Transactional
    Optional<OuterComponent> connectDataSetWithTestScenario(Integer scenarioId, Long dataSetId, Map<String, String> initMap) {
        try {
            OuterComponent testScenario = testScenarioService.getTestScenarioById(scenarioId);
            DataSet dataSet = dataSetRepository.findDataSetById(dataSetId).orElseThrow(() -> new DataSetNotFoundException("DatasSet was not found"));
            List<Step> stepsParams = testScenario.getSteps();
            for (Step s : stepsParams
            ) {
                for (Map.Entry<String, String> entry : s.getParameters().entrySet()) {
                    if (initMap.containsKey(entry.getValue())) {
                        entry.setValue(initMap.get(entry.getValue()));
                    }
                    if (dataSet.getParameters().containsKey(entry.getValue()) && !initMap.containsKey(entry.getValue())) {
                        entry.setValue(dataSetRepository.findValueByKeyAndId(dataSetId, entry.getValue()).orElseThrow(() -> new DataSetNotFoundException("DatasSet was not found")));
                    }
                }
            }
            return Optional.of(testScenario);
        } catch (TestScenarioNotFoundException e) {
            System.out.println(e.getMessage());
        }
        return Optional.empty();
    }

    @Transactional
    TestCase fromTestCaseResponseToModel(TestCaseResponse testCaseResponse) {
        TestCase testCase = modelMapper.map(testCaseResponse, TestCase.class);
        List<DataSet> dataSets = new ArrayList<>();

        testCaseResponse.getDataSetResponseList()
                .forEach(dataSetResponse -> dataSets.add(dataSetRepository.findDataSetById(dataSetResponse.getId()).orElseThrow(() -> new DataSetNotFoundException("DatasSet was not found"))));
        testCase.setDataSetList(dataSets);
        return testCase;
    }

    @Transactional
    public RunResult findById(Long id) {
        return runResultRepository.findRunResultById(id)
                .orElseThrow(() -> new RunResultNotFoundException("Run result not found"));
    }
}
