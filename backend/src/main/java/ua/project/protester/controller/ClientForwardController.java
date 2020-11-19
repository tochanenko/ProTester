package ua.project.protester.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ClientForwardController {
    @GetMapping(value = {"/login", "/register"})
    public String forward() {
        return "forward:/index.html";
    }
}
