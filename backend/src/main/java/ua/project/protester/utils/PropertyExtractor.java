package ua.project.protester.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;

import java.util.Objects;

@UtilityClass
@Slf4j
public class PropertyExtractor {

    private static final String PROPERTY_NOT_FOUND_TEMPLATE = "Could not find property '%s'";

    public static String extract(Environment env, String propertyName) {
        try {
            return Objects.requireNonNull(env.getProperty(propertyName));
        } catch (NullPointerException e) {
            log.warn(String.format(
                    PROPERTY_NOT_FOUND_TEMPLATE,
                    propertyName));
            return "";
        }
    }
}
