package ua.project.protester.conf;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.sql.DataSource;

@Configuration
@PropertySource("classpath:/application.properties")
public class DataSourceConfig {

        @Value("${spring.datasource.username}")
        private String name;

        @Value("${spring.datasource.password}")
        private String password;

        @Value("${spring.datasource.url}")
        private String url;

        @Value("${spring.datasource.driver-class-name}")
        private String driverName;

        @Bean
        public DataSource getDataSource() {
            DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
            dataSourceBuilder.driverClassName(driverName);
            dataSourceBuilder.url(url);
            dataSourceBuilder.username(name);
            dataSourceBuilder.password(password);
            return dataSourceBuilder.build();
        }

}
