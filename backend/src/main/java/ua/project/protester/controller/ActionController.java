package ua.project.protester.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ua.project.protester.exception.executable.action.ActionNotFoundException;
import ua.project.protester.model.executable.AbstractAction;
import ua.project.protester.model.executable.ExecutableComponentType;
import ua.project.protester.service.ActionService;
import ua.project.protester.utils.Page;

@RestController
@RequestMapping("/api/actions")
@RequiredArgsConstructor
public class ActionController {

    private final ActionService actionService;

    @GetMapping
    public Page<AbstractAction> getAllProjects(@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                           @RequestParam(value = "pageNumber", defaultValue = "1") Integer pageNumber,
                                           @RequestParam(value = "actionName", defaultValue = "") String actionName, @RequestParam(value = "actionType", required = false) String actionType) {
        ExecutableComponentType type = null;
        if (actionType != null) {
            type = ExecutableComponentType.valueOf(actionType);
        }
        return actionService.findAllProjects(pageSize, pageNumber, actionName, type);
    }

    @PutMapping("/{id}")
    public AbstractAction updateDescription(@PathVariable Integer id, @RequestParam String newDescription) {
        return actionService.updateDescription(id, newDescription);
    }


    @GetMapping("/{id}")
    public AbstractAction findActionById(@PathVariable Integer id) throws ActionNotFoundException {
        return actionService.findActionByActionId(id);
    }

}
