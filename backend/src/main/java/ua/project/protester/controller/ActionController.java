package ua.project.protester.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.project.protester.model.BaseAction;
import ua.project.protester.request.ActionRequestModel;
import ua.project.protester.service.ActionService;

import java.util.List;

@RestController
@RequestMapping("/api/actions")
@RequiredArgsConstructor
public class ActionController {

    private final ActionService actionService;

    @PostMapping
    public void saveActions(@RequestBody List<ActionRequestModel> actions) {
         actionService.prepareAndSaveActions(actions);
    }

    @PostMapping("/execute")
    public ResponseEntity<String> executeAllPreparedActions() {
        actionService.invokeAllPreparedStatements();
        return new ResponseEntity<>("Actions was triggered", HttpStatus.OK);
    }

    @PostMapping("execute/{id}")
    public ResponseEntity<String> executePreparedAction(@PathVariable Integer id) {
        actionService.invokePreparedActionByActionId(id);
        return new ResponseEntity<>("Action was triggered", HttpStatus.OK);
    }

    @GetMapping
    public List<BaseAction> actions() {
        return actionService.findAllEmptyActions();
    }

    @GetMapping("/prepared")
    public List<BaseAction> allPreparedActions() {
        return actionService.findAllPreparedActions();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePreparedAction(@PathVariable Integer id) {
        if (actionService.findPreparedActionByActionId(id) == null) {
            return new ResponseEntity<>("Action doesn`t exist!", HttpStatus.NOT_FOUND);
        }
        actionService.deletePreparedActionByActionId(id);
        return new ResponseEntity<>("Action was deleted", HttpStatus.OK);
    }
}
