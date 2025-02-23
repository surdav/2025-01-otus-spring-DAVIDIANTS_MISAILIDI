package ru.otus.hw.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.beans.factory.annotation.Value;
import ru.otus.hw.dao.CsvQuestionDao;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.service.IOService;
import ru.otus.hw.service.StreamsIOService;
import ru.otus.hw.service.StudentService;
import ru.otus.hw.service.StudentServiceImpl;
import ru.otus.hw.service.ResultService;
import ru.otus.hw.service.ResultServiceImpl;
import ru.otus.hw.service.TestService;
import ru.otus.hw.service.TestServiceImpl;
import ru.otus.hw.service.TestRunnerService;
import ru.otus.hw.service.TestRunnerServiceImpl;

@Configuration
@PropertySource("classpath:application.properties")
@ComponentScan({"ru.otus.hw"}) // Scan components in the specified package
public class AppConfig {

    @Bean
    public AppProperties appProperties(
            @Value("${test.rightAnswersCountToPass}") int rightAnswersCountToPass,
            @Value("${test.fileName}") String testFileName) {

        AppProperties appProperties = new AppProperties();

        appProperties.setRightAnswersCountToPass(rightAnswersCountToPass);

        appProperties.setTestFileName(testFileName);

        return appProperties;
    }

    @Bean
    public QuestionDao questionDao(AppProperties appProperties) {
        return new CsvQuestionDao(appProperties);
    }

    @Bean
    public IOService ioService() {
        return new StreamsIOService(System.out, System.in);
    }

    @Bean
    public StudentService studentService(IOService ioService) {
        return new StudentServiceImpl(ioService);
    }

    @Bean
    public ResultService resultService(AppProperties appProperties, IOService ioService) {
        return new ResultServiceImpl(appProperties, ioService);
    }

    @Bean
    public TestService testService(IOService ioService, QuestionDao questionDao) {
        return new TestServiceImpl(ioService, questionDao);
    }

    @Bean
    public TestRunnerService testRunnerService(
            TestService testService,
            StudentService studentService,
            ResultService resultService) {
        return new TestRunnerServiceImpl(testService, studentService, resultService);
    }
}