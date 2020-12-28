package ua.project.protester.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.project.protester.model.SystemInfo;

@RestController
@RequestMapping("/api/system-info")
public class SystemInfoController {

    @GetMapping
    public SystemInfo getSystemInfo() {
        return SystemInfo.get();
    }
}
