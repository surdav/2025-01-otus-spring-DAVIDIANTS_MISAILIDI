package ru.otus.hw;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.otus.hw.service.TestRunnerService;

public class Application {
    public static void main(String[] args) {

        // Create Spring context from XML configuration (spring-context.xml is placed in resources)
        ApplicationContext context = new ClassPathXmlApplicationContext("spring-context.xml");

        // Retrieve the TestRunnerService bean and run the test
        var testRunnerService = context.getBean(TestRunnerService.class);
        testRunnerService.run();

    }
}