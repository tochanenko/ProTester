package ua.project.protester.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ua.project.protester.exception.ScreenshotNotFoundException;
import ua.project.protester.repository.ScreenshotRepository;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScreenshotService {

    private final ScreenshotRepository screenshotRepository;

    public Map<String, String> getByName(String name) throws ScreenshotNotFoundException {
        try {
            return screenshotRepository.getByName(name);
        } catch (IOException e) {
            log.warn(e.getMessage());
            throw new ScreenshotNotFoundException("Failed to find screenshot with name " + name, e);
        }
    }
}
