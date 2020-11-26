package ua.project.protester.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.project.protester.model.BaseAction;
import ua.project.protester.service.ActionService;

import java.util.List;

@RestController
@RequestMapping("/api/actions")
@RequiredArgsConstructor
public class ActionController {

    private final ActionService actionService;

    @PostMapping("/execute")
    public ResponseEntity<String> executeActions(@RequestBody List<BaseAction> actions) {
        actions.forEach(actionService::invoke);
        return new ResponseEntity<>("Action was triggered", HttpStatus.OK);
    }

    @GetMapping
    public List<BaseAction> actions() {
        return actionService.findAll();
    }
}
