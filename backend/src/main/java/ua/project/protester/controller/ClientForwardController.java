package ua.project.protester.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ClientForwardController {
    @GetMapping(value = {"/login", "/register", "/forgot-password", "/pending-password", "/new-password", "/change-password", "/token-expired", "/actions", "/users_list", "/user",
            "/test-case-list", "/test-case-analyze" })
    public String forward() {
        return "forward:/index.html";
    }
}
