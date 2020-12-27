package ua.project.protester.service;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.core.JdbcTemplate;
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
import ua.project.protester.model.RunResult;
import ua.project.protester.model.DataSet;
import ua.project.protester.model.ActionWrapper;
import ua.project.protester.model.TestCaseWrapperResult;
import ua.project.protester.model.TestCaseDto;
import ua.project.protester.model.Environment;
import ua.project.protester.model.TestCase;
import ua.project.protester.model.executable.OuterComponent;
import ua.project.protester.model.executable.Step;
import ua.project.protester.model.executable.result.ActionResultDto;
import ua.project.protester.model.executable.result.ResultStatus;
import ua.project.protester.model.executable.result.TestCaseResultDto;
import ua.project.protester.repository.DataSetRepository;
import ua.project.protester.repository.OuterComponentRepository;
import ua.project.protester.repository.result.ActionResultRepository;
import ua.project.protester.repository.result.RunResultRepository;
import ua.project.protester.repository.result.TestCaseResultRepository;
import ua.project.protester.request.RunTestCaseRequest;
import ua.project.protester.response.ValidationDataSetResponse;
import ua.project.protester.response.ValidationDataSetStatus;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
public class StartService {

    private RestTemplate restTemplate;
    private DataSetRepository dataSetRepository;
    private TestScenarioService testScenarioService;
    private ModelMapper modelMapper;
    private ActionResultRepository actionResultRepository;
    private TestCaseResultRepository resultRepository;
    private UserService userService;
    private RunResultRepository runResultRepository;
    private SimpMessagingTemplate messagingTemplate;
    private List<TestCaseDto> testCasesDto = new ArrayList<>();
    private EnvironmentService environmentService;
    private OuterComponentRepository outerComponentRepository;
    private static int counter = 0;

    public StartService(RestTemplate restTemplate, DataSetRepository dataSetRepository, TestScenarioService testScenarioService, ModelMapper modelMapper, ActionResultRepository actionResultRepository, TestCaseResultRepository resultRepository, UserService userService, RunResultRepository runResultRepository, SimpMessagingTemplate messagingTemplate, EnvironmentService environmentService, OuterComponentRepository outerComponentRepository) {
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
        this.outerComponentRepository = outerComponentRepository;
    }

    public void execute(Long runId) {
        WebDriver driver = null;
        try {
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--disable-gpu");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--headless");
            options.addArguments("--lang=en");
            driver = new ChromeDriver(options);
            driver.manage().window().setSize(new Dimension(800, 600));
            driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
            RunResult runResult = runResultRepository.findRunResultById(runId).orElseThrow();
            List<TestCaseWrapperResult> testCaseResults = runResult.getTestCaseResults();
            for (int i = 0; i < testCasesDto.size(); i++) {
                runTestCase(testCasesDto.get(i), testCaseResults.get(i).getTestResultId(), driver);
            }

        } catch (Exception exception) {
            log.error("exception {}", exception.getClass().getName());
        } finally {
            log.info("driver was closed");
            Objects.requireNonNull(driver).quit();
            testCasesDto.clear();
        }
    }

    @Transactional
    void runTestCase(TestCaseDto testCaseDto, int testCaseResultId, @Lazy WebDriver webDriver) throws TestScenarioNotFoundException {
        TestCase testCase = modelMapper.map(testCaseDto, TestCase.class);
        DataSet dataSet = dataSetRepository.findDataSetById(testCase.getDataSetId())
                .orElseThrow(() -> new DataSetNotFoundException("Data set was not found"));
        Environment environment = getEnvironment(testCaseDto);
        try {
            testScenarioService.getTestScenarioById(testCase.getScenarioId().intValue()).execute(dataSet.getParameters(), getTemplate(testCaseDto), webDriver, restTemplate, environment, getConsumer(testCaseResultId));
            resultRepository.updateStatusAndEndDate(testCaseResultId, ResultStatus.PASSED, OffsetDateTime.now());
            counter = 0;
            closeConnection(environment);
        } catch (ActionExecutionException | IllegalActionLogicImplementation e) {
            resultRepository.updateStatusAndEndDate(testCaseResultId, ResultStatus.FAILED, OffsetDateTime.now());
            counter = 0;
            closeConnection(environment);
        }
    }

    @Transactional
    public RunResult getTestCaseExecutionResult(RunTestCaseRequest runTestCaseRequest) throws TestScenarioNotFoundException {

        RunResult resultFromDb = runResultRepository.saveRunResult(runTestCaseRequest.getUserId());

        resultFromDb.setTestCaseResults(runTestCaseRequest.getTestCaseResponseList().stream()
                .map(response -> {
                    TestCaseResultDto testCaseResult = new TestCaseResultDto(userService.findUserById(response.getAuthorId()).orElseThrow(), modelMapper.map(response, TestCase.class));
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
            TestCaseDto currentTestCaseDto = runTestCaseRequest.getTestCaseResponseList().get(i);
            TestCaseWrapperResult currentTestCaseWrapperResult = resultFromDb.getTestCaseResults().get(i);
            List<Step> steps = findStepsRecursively(testScenarioService.getTestScenarioById(currentTestCaseDto.getScenarioId().intValue()).getSteps()
                    .stream())
                    .collect(Collectors.toList());
            runResultRepository.saveActionWrappersByTestCaseResultWrapperId(currentTestCaseWrapperResult.getId(), steps);
        }
        testCasesDto = runTestCaseRequest.getTestCaseResponseList();

        return resultFromDb;
    }


    @Transactional
    Consumer<ActionResultDto> getConsumer(Integer testCaseResultId) {
        return (action) -> {
            try {
                List<ActionWrapper> actionWrappers = runResultRepository.findActionWrapperByTestCaseResult(testCaseResultId,
                        runResultRepository.findScenarioIdByTestCaseWrapperResult(testCaseResultId), false);
                ActionResultDto actionResultDto = actionResultRepository.save(testCaseResultId, action);
                actionResultDto.setEndDateStr(LocalDateTime.from(actionResultDto.getEndDate()).format(DateTimeFormatter.ISO_DATE_TIME));
                actionResultDto.setStartDateStr(LocalDateTime.from(actionResultDto.getStartDate()).format(DateTimeFormatter.ISO_DATE_TIME));
                actionWrappers.get(counter).setActionResultDtoId(actionResultDto.getId());

                if (counter == actionWrappers.size() - 1 || actionResultDto.getStatus().equals(ResultStatus.FAILED)) {
                    actionResultDto.setLast(true);
                }

                messagingTemplate.convertAndSend("/topic/public/" + actionWrappers.get(counter).getId(), actionResultDto);
                counter++;

            } catch (TestScenarioNotFoundException | IllegalActionLogicImplementation  illegalActionLogicImplementation) {
                illegalActionLogicImplementation.printStackTrace();
            }
        };
    }

    @Transactional
    public RunResult findById(Long id) {
        return runResultRepository.findRunResultById(id)
                .orElseThrow(() -> new RunResultNotFoundException("Run result not found"));
    }

    private Stream<Step> findStepsRecursively(Stream<Step> initial) {
        return initial
                .flatMap(s -> {
                    if (s.isAction()) {
                        return Stream.of(s);
                    } else {
                        return findStepsRecursively(outerComponentRepository.findOuterComponentById(s.getComponent().getId(), true).getSteps().stream());
                    }
                });
    }

    @Transactional
    public ValidationDataSetResponse validateDataSetWithTestScenario(TestCaseDto testCaseDto) throws TestScenarioNotFoundException {
        DataSet dataSet = dataSetRepository.findDataSetById(testCaseDto.getDataSetId())
                .orElseThrow(() -> new DataSetNotFoundException("DataSet was not found"));
        OuterComponent testScenario = testScenarioService.getTestScenarioById(testCaseDto.getScenarioId().intValue());
        List<String> names = Arrays.asList(testScenario.getParameterNames());
        Set<String> dataSetKeys = dataSet.getParameters().keySet();

        ValidationDataSetResponse validationResponse = new ValidationDataSetResponse();

        validationResponse.setMissingParameters(names.stream().filter(value -> !dataSetKeys.contains(value)).collect(Collectors.toList()));

        validationResponse.setStatus(validationResponse.getMissingParameters().size() > 0 ? ValidationDataSetStatus.FAILED : ValidationDataSetStatus.PASSED);

        validationResponse.setDataSetName(dataSet.getName());

        return validationResponse;
    }

    private Environment getEnvironment(TestCaseDto testCaseDto) {
        if (testCaseDto.getEnvironmentId() != null) {
            Environment environment = environmentService.findById(testCaseDto.getEnvironmentId()).orElseThrow(() -> new EnvironmentNotFoundException("Environment was not found"));
            environment.setDataSource(createDataSource(environment));
            return environment;
        }
        return new Environment();
    }

    private JdbcTemplate getTemplate(TestCaseDto testCaseDto) {
        if (testCaseDto.getEnvironmentId() != null) {
            Environment environment = environmentService.findById(testCaseDto.getEnvironmentId())
                    .orElseThrow(() -> new EnvironmentNotFoundException("Environment was not found"));
            environment.setDataSource(createDataSource(environment));
            return new JdbcTemplate(environment.getDataSource());
        }
        return null;
    }

    private DataSource createDataSource(Environment environment) {
        if (environment.getId() != null) {
            DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
            dataSourceBuilder.driverClassName("org.postgresql.Driver");
            dataSourceBuilder.url(environment.getUrl());
            dataSourceBuilder.username(environment.getUsername());
            dataSourceBuilder.password(environment.getPassword());
            return dataSourceBuilder.build();
        }
        return null;
    }

    private void closeConnection(Environment environment) {
        if (environment.getDataSource() != null) {
            try {
                environment.getDataSource().getConnection().close();
            } catch (SQLException e) {
                log.warn("SQL exception{}", e.getClass().getName());
            }
        }
    }
}
