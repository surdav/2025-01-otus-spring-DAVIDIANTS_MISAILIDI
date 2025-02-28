package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

import java.util.List;

@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final IOService ioService;

    private final QuestionDao questionDao;

    private final LocalizationService localizationService; // Adding LocalizationService

    @Override
    public TestResult executeTestFor(Student student) {
        ioService.printLine("");
        // Output localized message for answering questions
        ioService.printLine(localizationService.getMessage("TestService.answer.the.questions"));

        List<Question> questions = questionDao.findAll();
        TestResult testResult = new TestResult(student);

        for (int i = 0; i < questions.size(); i++) {
            Question question = questions.get(i);
            printQuestionWithAnswers(question, i + 1);
            boolean isAnswerCorrect = handleAnswerInput(question);
            testResult.applyAnswer(question, isAnswerCorrect);
        }
        return testResult;
    }

    private void printQuestionWithAnswers(Question question, int questionNumber) {
        ioService.printFormattedLine("%d. %s", questionNumber, question.text());
        List<Answer> answers = question.answers();

        if (answers != null && !answers.isEmpty()) {
            int answerNumber = 1;
            for (Answer answer : answers) {
                ioService.printFormattedLine("   %d) %s", answerNumber, answer.text());
                answerNumber++;
            }
        }
        ioService.printLine("");
    }

    private boolean handleAnswerInput(Question question) {
        List<Answer> answers = question.answers();
        if (answers == null || answers.isEmpty()) {
            return false;
        }
        // Use localized prompts for input and validation
        int userAnswer = ioService.readIntForRangeWithPrompt(
                1, answers.size(),
                localizationService.getMessage("TestService.select.correct.answer"),
                localizationService.getMessage("TestService.invalid.input", new Object[]{answers.size()})
        );
        return answers.get(userAnswer - 1).isCorrect();
    }
}