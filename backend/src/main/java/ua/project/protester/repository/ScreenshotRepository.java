package ua.project.protester.repository;

import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;
import java.util.Map;

@Repository
public class ScreenshotRepository {

    public Map<String, String> getByName(String name) throws IOException {
        File imageFile = new File("~\\screenshots\\" + name + ".png");
        String encodedImage = Base64.getEncoder().withoutPadding().encodeToString(Files.readAllBytes(imageFile.toPath()));
        return Map.of("content", encodedImage);
    }
}
