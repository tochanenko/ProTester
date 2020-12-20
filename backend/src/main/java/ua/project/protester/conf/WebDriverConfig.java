package ua.project.protester.conf;

import okhttp3.OkHttpClient;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.PropertySource;

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
    public OkHttpClient okHttpClient() {
        return new OkHttpClient();
    }
}
