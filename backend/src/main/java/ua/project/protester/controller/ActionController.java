package ua.project.protester.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.project.protester.model.executable.AbstractAction;
import ua.project.protester.request.ActionRequestModel;
import ua.project.protester.service.ActionService;

import java.util.List;

@RestController
@RequestMapping("/api/actions")
@RequiredArgsConstructor
public class ActionController {

    private final ActionService actionService;

    @GetMapping
    public List<AbstractAction> findAllActions() {
        return actionService.findAllActions();
    }

    @PostMapping("/execute")
    public ResponseEntity<String> executeAllPreparedActions(@RequestBody List<ActionRequestModel> actions) {
        actionService.invoke(actions);
        return new ResponseEntity<>("Actions was triggered", HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public AbstractAction findActionById(@PathVariable Integer id) {
        return actionService.findActionByActionId(id);
    }

}
