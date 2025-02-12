package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;

import java.util.List;

// TestServiceImpl retrieves questions from the DAO and uses IOService to print them to the console
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final IOService ioService;

    private final QuestionDao questionDao;

    @Override
    public void executeTest() {
        List<Question> questions = questionDao.findAll();
        ioService.printLine("Please answer the following questions:\n");
        int questionNumber = 1;
        for (Question question : questions) {
            printQuestionWithAnswers(question, questionNumber);
            questionNumber++;
        }
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
        ioService.printLine(""); // Blank line between questions
    }
}
