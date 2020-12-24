package ua.project.protester.conf;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import javax.validation.constraints.NotNull;

@Configuration
@PropertySource("classpath:/webdriver/webdriver.properties")
public class WebDriverConfig {

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
