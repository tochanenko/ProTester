package ua.project.protester.conf;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.*;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import javax.validation.constraints.NotNull;

@Configuration
@PropertySource("classpath:/webdriver/webdriver.properties")
public class WebDriverConfig {

    @Lazy
    @Bean
    public WebDriver getDriver() {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\vovan\\Downloads\\chromedriver_win32\\chromedriver.exe");
        return new ChromeDriver();
    }

    @Bean
    @Scope("prototype")
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder, ResponseErrorHandler responseErrorHandler) {
        return restTemplateBuilder
                .errorHandler(responseErrorHandler)
                .build();
    }

    @Bean
    public ResponseErrorHandler responseErrorHandler() {
        return new ResponseErrorHandler() {
            @Override
            public boolean hasError(@NotNull ClientHttpResponse response) {
                return false;
            }

            @Override
            public void handleError(@NotNull ClientHttpResponse response) {
            }
        };
    }
}
