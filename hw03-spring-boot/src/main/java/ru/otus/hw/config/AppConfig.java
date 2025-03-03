package ru.otus.hw.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;
import ru.otus.hw.dao.CsvQuestionDao;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.service.IOService;
import ru.otus.hw.service.TestRunnerServiceImpl;
import ru.otus.hw.service.LocalizedIOService;
import ru.otus.hw.service.TestRunnerService;
import ru.otus.hw.service.TestServiceImpl;
import ru.otus.hw.service.TestService;
import ru.otus.hw.service.ResultServiceImpl;
import ru.otus.hw.service.ResultService;
import ru.otus.hw.service.StudentServiceImpl;
import ru.otus.hw.service.LocalizationService;
import ru.otus.hw.service.StudentService;
import ru.otus.hw.service.StreamsIOService;

@Service
@ComponentScan({"ru.otus.hw"}) // Scan components in the specified package
public class AppConfig {

    @Bean
    public QuestionDao questionDao(AppProperties appProperties) {
        return new CsvQuestionDao(appProperties);
    }

    @Bean
    public IOService ioService() {
        return new StreamsIOService(System.out, System.in);
    }

    @Bean
    public StudentService studentService(IOService ioService, LocalizationService localizationService) {
        return new StudentServiceImpl(ioService, localizationService);
    }

    @Bean
    public ResultService resultService(
            AppProperties appProperties,
            IOService ioService,
            LocalizationService localizationService) {
        return new ResultServiceImpl(appProperties, ioService, localizationService);
    }

    @Bean
    public TestService testService(
            IOService ioService,
            QuestionDao questionDao,
            LocalizationService localizationService) {
        return new TestServiceImpl(ioService, questionDao, localizationService);
    }

    @Bean
    public TestRunnerService testRunnerService(
            TestService testService,
            StudentService studentService,
            ResultService resultService,
            LocalizationService localizationService,
            LocalizedIOService localizedIOService) {
        return new TestRunnerServiceImpl(
                testService,
                studentService,
                resultService,
                localizationService,
                localizedIOService);
    }
}