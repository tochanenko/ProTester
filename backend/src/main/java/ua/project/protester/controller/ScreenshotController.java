package ua.project.protester.controller;

import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;

// TODO: For testing
@RestController
@RequestMapping("/api/screenshots")
@RequiredArgsConstructor
public class ScreenshotController {

    @GetMapping("/{name}")
    public byte[] test(@PathVariable String name) {
        try {
            return FileUtils.readFileToByteArray(new File("~\\screenshots\\" + name + ".png"));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
