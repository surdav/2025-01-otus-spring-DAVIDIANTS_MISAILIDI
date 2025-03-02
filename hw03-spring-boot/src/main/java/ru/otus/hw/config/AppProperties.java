package ru.otus.hw.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "test") // Binds the “test” prefix from application.yml
public class AppProperties implements TestConfig, TestFileNameProvider {

    // Inject property "test.rightAnswersCountToPass" from application.yml
    private int rightAnswersCountToPass;

    // Inject property "test.fileName" from application.yml
    private String fileName;
}
