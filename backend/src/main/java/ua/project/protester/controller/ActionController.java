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


    @GetMapping("/name")
    public BaseAction findByName(@RequestParam String name) {
        return actionService.findActionByName(name);
    }

    @GetMapping("/type")
    public BaseAction findByType(@RequestParam String type) {
        return actionService.findActionByType(type);
    }

    @GetMapping("/page")
    public List<BaseAction> findActionsPagination(@RequestParam int pageSize, @RequestParam int pageNumber) {
        return actionService.findActionsPagination(pageSize, pageNumber);
    }
    /*
    * {
    *    "id": 43,
    *    "name": "Login with username "username" and password "password",
    *    "params": [
    *       {
    *           "name": "username"
    *       },
    *       {
    *           "password": "password"
    *       }
    *   ],
    *   "actions": [434, 343, 656]
    * }
    * */


    /*
    * Login with username "admin" and password "root"
    *   Find input with id "username_fiend"  and set value "${username}"
    *   Find input with id "password_fiend"  and set value "${password}"
    *   Find button with id "${loginBtn}" and click it
    * Find button with id "${loginBtn}" and click it

     *
    * */

//    loginBtn=log_btn
}
