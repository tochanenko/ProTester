package ua.project.protester.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.project.protester.exception.ScreenshotNotFoundException;
import ua.project.protester.service.ScreenshotService;

import java.util.Map;

@PreAuthorize("isAuthenticated()")
@RestController
@RequestMapping("/api/screenshots")
@RequiredArgsConstructor
@Slf4j
public class ScreenshotController {

    private final ScreenshotService screenshotService;

    @GetMapping("/{name}")
    public Map<String, String> test(@PathVariable String name) throws ScreenshotNotFoundException {
        return screenshotService.getByName(name);
    }
}
