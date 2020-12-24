package ua.project.protester.service;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.context.annotation.Lazy;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import ua.project.protester.exception.DataSetNotFoundException;
import ua.project.protester.exception.EnvironmentNotFoundException;
import ua.project.protester.exception.executable.action.ActionExecutionException;
import ua.project.protester.exception.executable.action.IllegalActionLogicImplementation;
import ua.project.protester.exception.executable.scenario.TestScenarioNotFoundException;
import ua.project.protester.exception.result.RunResultNotFoundException;
import ua.project.protester.model.TestCaseWrapperResult;
import ua.project.protester.model.RunResult;
import ua.project.protester.model.Environment;
import ua.project.protester.model.TestCase;
import ua.project.protester.model.DataSet;
import ua.project.protester.model.ActionWrapper;
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
import ua.project.protester.response.ValidationDataSetResponse;
import ua.project.protester.response.ValidationDataSetStatus;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Service
@Slf4j
public class StartService {

    private  RestTemplate restTemplate;
    private DataSetRepository dataSetRepository;
    private TestScenarioService testScenarioService;
    private ModelMapper modelMapper;
    private ActionResultRepository actionResultRepository;
    private TestCaseResultRepository resultRepository;
    private UserService userService;
    private RunResultRepository runResultRepository;
    private SimpMessagingTemplate messagingTemplate;
    private List<TestCaseResponse> testCaseResponses;
    private EnvironmentService environmentService;
    private static int counter = 0;

    public StartService(RestTemplate restTemplate, DataSetRepository dataSetRepository, TestScenarioService testScenarioService, ModelMapper modelMapper, ActionResultRepository actionResultRepository, TestCaseResultRepository resultRepository, UserService userService, RunResultRepository runResultRepository, SimpMessagingTemplate messagingTemplate, EnvironmentService environmentService) {
        this.restTemplate = restTemplate;
        this.dataSetRepository = dataSetRepository;
        this.testScenarioService = testScenarioService;
        this.modelMapper = modelMapper;
        this.actionResultRepository = actionResultRepository;
        this.resultRepository = resultRepository;
        this.userService = userService;
        this.runResultRepository = runResultRepository;
        this.messagingTemplate = messagingTemplate;
        this.environmentService = environmentService;
    }

    public void execute(Long id) throws TestScenarioNotFoundException {

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--headless");
        options.addArguments("--lang=en");
        WebDriver driver = new ChromeDriver(options);
        RunResult runResult = runResultRepository.findRunResultById(id).orElseThrow();
        List<TestCaseWrapperResult> testCaseResults = runResult.getTestCaseResults();
          for (int i = 0; i < testCaseResponses.size(); i++) {
            runTestCase(testCaseResponses.get(i), testCaseResults.get(i).getTestResultId(), driver);
        }
          driver.quit();
        log.info("testCaseResponses are {}", testCaseResponses);
        testCaseResponses.clear();
    }

    @Transactional
    void runTestCase(TestCaseResponse testCaseResponse, int testCaseResultId, @Lazy WebDriver webDriver) throws TestScenarioNotFoundException {
        TestCase testCase = fromTestCaseResponseToModel(testCaseResponse);
        DataSet dataSet = testCase.getDataSetList().get(0);
        Environment environment = checkSQLEnvironment(testCaseResponse);
        log.info("dataset{}", dataSet);
        try {
            testScenarioService.getTestScenarioById(testCase.getScenarioId().intValue()).execute(dataSet.getParameters(), environment, webDriver, restTemplate, getConsumer(testCaseResultId));
            resultRepository.updateStatusAndEndDate(testCaseResultId, ResultStatus.PASSED, OffsetDateTime.now());
            counter = 0;
        } catch (ActionExecutionException | IllegalActionLogicImplementation e) {
            resultRepository.updateStatusAndEndDate(testCaseResultId, ResultStatus.FAILED, OffsetDateTime.now());
            counter = 0;
        }
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
        testCaseResponses = runTestCaseRequest.getTestCaseResponseList();

        return resultFromDb;
    }


    @Transactional
    Consumer<ActionResultDto> getConsumer(Integer testCaseResultId) {
        return (action) -> {
            try {
                log.info("action {}",  action);
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
    TestCase fromTestCaseResponseToModel(TestCaseResponse testCaseResponse) {
        TestCase testCase = modelMapper.map(testCaseResponse, TestCase.class);
        List<DataSet> dataSets = new ArrayList<>();

        testCaseResponse.getDataSetResponseList()
                .forEach(dataSetResponse -> dataSets.add(dataSetRepository.findDataSetById(dataSetResponse.getId()).orElseThrow(() -> new DataSetNotFoundException("DataSet was not found"))));
        testCase.setDataSetList(dataSets);
        return testCase;
    }

    @Transactional
    public RunResult findById(Long id) {
        return runResultRepository.findRunResultById(id)
                .orElseThrow(() -> new RunResultNotFoundException("Run result not found"));
    }

    public ValidationDataSetResponse validateDataSetWithTestScenario(TestCaseResponse testCaseResponse) throws TestScenarioNotFoundException {
       log.info("test case {}", testCaseResponse);
        DataSet dataSet = dataSetRepository.findDataSetById(testCaseResponse.getDataSetResponseList().stream().filter(Objects::nonNull).findFirst().get().getId()).orElseThrow(() -> new DataSetNotFoundException("DataSet was`nt found"));
        OuterComponent testScenario = testScenarioService.getTestScenarioById(testCaseResponse.getScenarioId().intValue());
        List<String> names = Arrays.asList(testScenario.getParameterNames());
        Set<String> dataSetKeys = dataSet.getParameters().keySet();

        ValidationDataSetResponse validationResponse = new ValidationDataSetResponse();

        validationResponse.setMissingParameters(names.stream().filter(value -> !dataSetKeys.contains(value)).collect(Collectors.toList()));

        validationResponse.setStatus(validationResponse.getMissingParameters().size() > 0 ? ValidationDataSetStatus.FAILED : ValidationDataSetStatus.PASSED);

        validationResponse.setDataSetName(dataSet.getName());

        return validationResponse;
    }

    private Environment checkSQLEnvironment(TestCaseResponse testCaseResponse) {
        if (testCaseResponse.getEnvironmentId() == null) {
            return new Environment();
        }
        return environmentService.findById(testCaseResponse.getEnvironmentId()).orElseThrow(() -> new EnvironmentNotFoundException("Environment was not found"));
    }
}
