package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TestRunnerServiceImpl implements TestRunnerService {

    private final TestService testService;

    private final StudentService studentService;

    private final ResultService resultService;

    private final LocalizationService localizationService;

    private final LocalizedIOService localizedIOService;

    @Override
    public void run() {
        // Output a localized message indicating the test is starting
        localizedIOService.printLineLocalized(localizationService.getMessage("TestRunner.starting"));

        // Determine the student
        var student = studentService.determineCurrentStudent();

        // Execute the test for the student
        var testResult = testService.executeTestFor(student);

        // Show the test result
        resultService.showResult(testResult);

        // Output a localized message indicating the test is complete
        localizedIOService.printLineLocalized(localizationService.getMessage("TestRunner.completed"));
    }
}