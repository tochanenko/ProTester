package ua.project.protester.conf;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.context.annotation.*;
import org.springframework.web.client.RestTemplate;

@Configuration
@PropertySource("classpath:/webdriver/webdriver.properties")
public class WebDriverConfig {

    @Lazy
    @Bean
    @Scope("prototype")
    public WebDriver getDriver() {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\vovan\\Downloads\\chromedriver_win32\\chromedriver.exe");
        return new ChromeDriver();
    }

    @Bean
    @Scope("prototype")
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
