package ua.project.protester.run;

import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import ua.project.protester.action.ClickOnElementWithIdAction;
import ua.project.protester.action.ClickOnLinkWithTextAction;
import ua.project.protester.action.GoToUrlAction;
import ua.project.protester.action.InputTextIntoFieldWithIdAction;
import ua.project.protester.exception.executable.OuterComponentNotFoundException;
import ua.project.protester.exception.executable.compound.CompoundNotFoundException;
import ua.project.protester.model.executable.AbstractAction;
import ua.project.protester.model.executable.OuterComponent;
import ua.project.protester.model.executable.Step;
import ua.project.protester.model.executable.result.AbstractActionResult;
import ua.project.protester.repository.ActionRepository;
import ua.project.protester.repository.OuterComponentRepository;
import ua.project.protester.request.OuterComponentRepresentation;
import ua.project.protester.request.StepRepresentation;
import ua.project.protester.service.ActionService;
import ua.project.protester.service.CompoundService;
import ua.project.protester.service.TestScenarioService;

import java.awt.geom.RectangularShape;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Component
public class RunTestCase {

    private WebDriver webDriver;
    private Consumer<AbstractActionResult> resultCallback = System.out::println;
    private ActionService service;
    private OuterComponentRepository outerComponentRepository;
    @Autowired
    private TestScenarioService testScenarioService;
    @Autowired
    private CompoundService  compoundService;
    private Integer scenarioId;
    private List<Long> dataset;

    @Autowired
    public RunTestCase(@Lazy WebDriver webDriver, ActionService service, OuterComponentRepository outerComponentRepository) {
        this.webDriver = webDriver;
        this.service = service;
        this.outerComponentRepository = outerComponentRepository;
    }

    private OuterComponent loginCompound = new OuterComponent();
    private OuterComponent rememberMeCompound = new OuterComponent();

    private Map<String, String> clickOnElementWithIdStepMap = new HashMap<>();
    private Map<String, String> inputTextIntoFieldWithIdActionLoginStepMap = new HashMap<>();
    private Map<String, String> inputTextIntoFieldWithIdActionPasswordStepMap = new HashMap<>();
    private Map<String, String> rememberMeCompoundStepMap = new HashMap<>();
    private Map<String, String> clickOnLinkWithTextActionStepMap = new HashMap<>();


    public void run () throws OuterComponentNotFoundException, CompoundNotFoundException {


        AbstractAction clickOnElementWithIdAction = service.findActionByActionId(5);
        AbstractAction inputTextIntoFieldWithIdActionLogin = service.findActionByActionId(3);
        AbstractAction inputTextIntoFieldWithIdActionPassword = service.findActionByActionId(3);
        AbstractAction clickOnLinkWithTextAction = service.findActionByActionId(4);



        Step clickOnElementWithIdStep = new Step(1, true, clickOnElementWithIdAction, clickOnElementWithIdStepMap);
        Step inputTextIntoFieldWithIdActionLoginStep = new Step(2,true, inputTextIntoFieldWithIdActionLogin, inputTextIntoFieldWithIdActionLoginStepMap);
        Step inputTextIntoFieldWithIdActionPasswordStep = new Step(3,true, inputTextIntoFieldWithIdActionPassword, inputTextIntoFieldWithIdActionPasswordStepMap);

        Step rememberMeCompoundStep = new Step(4,false, rememberMeCompound, rememberMeCompoundStepMap);
        Step clickOnLinkWithTextActionStep = new Step(5, true, clickOnLinkWithTextAction, clickOnLinkWithTextActionStepMap);


        clickOnElementWithIdStepMap.put("id", "rzk_id");

        inputTextIntoFieldWithIdActionLoginStepMap.put("text", "${login}");
        inputTextIntoFieldWithIdActionLoginStepMap.put("id", "lgn_id");

        inputTextIntoFieldWithIdActionPasswordStepMap.put("text", "${password}");
        inputTextIntoFieldWithIdActionPasswordStepMap.put("id", "pwd_id");

        clickOnLinkWithTextActionStepMap.put("text", "txt_save_id");



        Map<String, String> initMap = new HashMap<>();
        initMap.put("login", "volodya");
        initMap.put("password", "tank85943221");
        initMap.put("url","www.youtube.com");
        List<Step> steps = new ArrayList<>();
        steps.add(clickOnElementWithIdStep);
        steps.add(inputTextIntoFieldWithIdActionLoginStep);
        steps.add(inputTextIntoFieldWithIdActionPasswordStep);
        steps.add(rememberMeCompoundStep);
        rememberMeCompound.setSteps(List.of(clickOnLinkWithTextActionStep));
        loginCompound.setSteps(steps);
        OuterComponentRepresentation loginTestScenario = new OuterComponentRepresentation();
        loginTestScenario.setName("login test scenario");
        loginTestScenario.setDescription("login test description");
        loginTestScenario.setSteps(steps.stream()
                .map( step -> new StepRepresentation(step.getId(), step.isAction() ,step.getParameters()))
                .collect(Collectors.toList()));
        OuterComponent result = outerComponentRepository.findOuterComponentById(18, true).get();
        System.out.println(result);
      //  result.getSteps().forEach(step -> step.getComponent().execute(initMap, webDriver, resultCallback));
    }

    public void secondRun() {
        try {
            OuterComponent outerComponent = outerComponentRepository.findOuterComponentById(15, false).get();
            outerComponent.getSteps().forEach( step -> outerComponent.execute(step.getParameters(), webDriver, resultCallback));
        } catch (OuterComponentNotFoundException e) {
            e.printStackTrace();
        }

    }
}
