package ua.project.protester.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.project.protester.request.TestCaseRequest;
import ua.project.protester.service.StartService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/test")
public class RunController {
    private final StartService startService;

    @PostMapping
    @SendTo("/result")
//    @SendToUser("")
    public void run(@RequestBody List<TestCaseRequest> testCase)  {
        startService.execute(testCase);
    }


}
