package ua.project.protester.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;
import java.util.Collections;
import java.util.Map;

@PreAuthorize("isAuthenticated()")
@RestController
@RequestMapping("/api/screenshots")
@RequiredArgsConstructor
@Slf4j
public class ScreenshotController {

    @GetMapping("/{name}")
    public Map<String, String> test(@PathVariable String name) {
        try {
            File imageFile = new File("~\\screenshots\\" + name + ".png");
            String encodedImage = Base64.getEncoder().withoutPadding().encodeToString(Files.readAllBytes(imageFile.toPath()));
            return Map.of("content", encodedImage);
        } catch (IOException e) {
            log.warn(e.getMessage());
            return Collections.emptyMap();
        }
    }
}
