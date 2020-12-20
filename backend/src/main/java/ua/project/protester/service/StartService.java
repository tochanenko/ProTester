package ua.project.protester.service;

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
import ua.project.protester.model.RunResult;
import ua.project.protester.model.TestCaseWrapperResult;
import ua.project.protester.model.Environment;
import ua.project.protester.model.TestCase;
import ua.project.protester.model.DataSet;
import ua.project.protester.model.ActionWrapper;
import ua.project.protester.model.executable.OuterComponent;
import ua.project.protester.model.executable.Step;
import ua.project.protester.model.executable.result.ActionResultDto;
import ua.project.protester.model.executable.result.ResultStatus;
import ua.project.protester.model.executable.result.TestCaseResultDto;
import ua.project.protester.model.executable.result.subtype.ActionResultTechnicalDto;
import ua.project.protester.repository.DataSetRepository;
import ua.project.protester.repository.result.ActionResultRepository;
import ua.project.protester.repository.result.RunResultRepository;
import ua.project.protester.repository.result.TestCaseResultRepository;
import ua.project.protester.request.RunTestCaseRequest;
import ua.project.protester.response.TestCaseResponse;

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

         RunResult runResult = runResultRepository.findRunResultById(id).get();
         List<TestCaseWrapperResult> testCaseResults = runResult.getTestCaseResults();
         testCaseResults.forEach(runResults -> runResults.getActionWrapperList().stream().forEach(System.out::println));
         log.info("run result {}", runResult);
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

        Environment environment = new Environment();
        TestCase testCase = fromTestCaseResponseToModel(testCaseResponse);
        testCase.getDataSetList().stream()
                    .map(DataSet::getId)
                    .map(id -> connectDataSetWithTestScenario(testCase.getScenarioId().intValue(), id, initMap))
                    .filter(Objects::nonNull)
                    .forEachOrdered(outerComponent -> {
                                try {
                                    outerComponent.get().execute(initMap, environment, webDriver, getConsumer(testCaseResultId));
                                    resultRepository.updateStatusAndEndDate(testCaseResultId, ResultStatus.PASSED, OffsetDateTime.now());
                                    counter = 0;
                                } catch (ActionExecutionException a) {
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
                    TestCaseResultDto testCaseResult = new TestCaseResultDto(userService.findUserById(response.getAuthorId()).get(), fromTestCaseResponseToModel(response));
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
                List<Step> step = testScenarioService.getTestScenarioById(currentTestCaseResponse.getScenarioId().intValue()).getSteps().stream()
                        .collect(Collectors.toList());
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
                actionWrappers.get(counter).setActionResultDtoId(actionResultDto.getId());
                switch (actionResultDto.getAction().getType()) {
                    case TECHNICAL:
                        ActionResultTechnicalDto actionResultUiDto = (ActionResultTechnicalDto) actionResultDto;
                        log.info("action result {}", actionResultUiDto.getClass().getName());
                        log.info("actionWrapper {}", actionWrappers.get(counter));
                        messagingTemplate.convertAndSend("/topic/public/" + actionWrappers.get(counter).getId(), actionResultUiDto);
                        counter++;
                        break;
                    case SQL:

                        counter++;
                        break;
                    default: throw new RuntimeException();
                }
            } catch (TestScenarioNotFoundException | IllegalActionLogicImplementation illegalActionLogicImplementation) {
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
                    if (initMap.containsKey(entry.getValue())) {
                        entry.setValue(initMap.get(entry.getValue()));
                    }
                    if (dataSet.getParameters().containsKey(entry.getValue()) && !initMap.containsKey(entry.getValue())) {
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
    TestCase fromTestCaseResponseToModel(TestCaseResponse testCaseResponse) {
        TestCase testCase = modelMapper.map(testCaseResponse, TestCase.class);
        List<DataSet> dataSets = new ArrayList<>();

        testCaseResponse.getDataSetResponseList()
                .forEach(dataSetResponse -> dataSets.add(dataSetRepository.findDataSetById(dataSetResponse.getId()).get()));
        testCase.setDataSetList(dataSets);
        return testCase;
    }

    @Transactional
    public RunResult findById(Long id) {
         return runResultRepository.findRunResultById(id)
                 .orElseThrow(RuntimeException::new);
    }
}
