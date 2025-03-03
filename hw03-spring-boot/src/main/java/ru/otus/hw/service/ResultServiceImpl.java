package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import ru.otus.hw.config.TestConfig;
import ru.otus.hw.domain.TestResult;

@RequiredArgsConstructor
public class ResultServiceImpl implements ResultService {

    private final TestConfig testConfig;

    private final IOService ioService;

    private final LocalizationService localizationService;

    @Override
    public void showResult(TestResult testResult) {
        ioService.printLine(""); // Print a blank line for spacing
        ioService.printLine(localizationService.getMessage("ResultService.test.results"));

        // Substitute placeholders with actual values
        ioService.printFormattedLine(localizationService.getMessage("ResultService.student",
                new Object[]{testResult.getStudent().getFullName()}));

        ioService.printFormattedLine(localizationService.getMessage("ResultService.answered.questions.count",
                new Object[]{testResult.getAnsweredQuestions().size()}));

        ioService.printFormattedLine(localizationService.getMessage("ResultService.right.answers.count",
                new Object[]{testResult.getRightAnswersCount()}));

        if (testResult.getRightAnswersCount() >= testConfig.getRightAnswersCountToPass()) {
            ioService.printLine(localizationService.getMessage("ResultService.passed.test"));
            return;
        }
        ioService.printLine(localizationService.getMessage("ResultService.fail.test"));
    }
}