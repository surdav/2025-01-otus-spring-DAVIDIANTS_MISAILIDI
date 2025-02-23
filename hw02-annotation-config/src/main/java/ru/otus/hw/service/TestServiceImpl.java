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

    @Override
    public TestResult executeTestFor(Student student) {
        ioService.printLine("");
        ioService.printFormattedLine("Please answer the questions below%n");

        // Retrieve questions from the DAO
        List<Question> questions = questionDao.findAll();

        // Create a TestResult object to store student's results
        TestResult testResult = new TestResult(student);

        // Iterate over all questions
        for (int i = 0; i < questions.size(); i++) {
            Question question = questions.get(i);

            // Print the question with answer options
            printQuestionWithAnswers(question, i + 1);

            // Get user input and validate the answer
            boolean isAnswerCorrect = handleAnswerInput(question);

            // Save the answer result
            testResult.applyAnswer(question, isAnswerCorrect);
        }
        return testResult;
    }

    /**
     * Prints a question and its available answer options.
     *
     * @param question       the question to display
     * @param questionNumber the number of the question to display
     */
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

    /**
     * Handles user input for a question and validates their answer.
     *
     * @param question the question for which the answer is being handled
     * @return true if the user provided a correct answer, otherwise false
     */
    private boolean handleAnswerInput(Question question) {
        List<Answer> answers = question.answers();

        if (answers == null || answers.isEmpty()) {
            return false; // If no answers are available, the answer cannot be valid
        }

        // Prompt the user to select an answer
        int userAnswer = ioService.readIntForRangeWithPrompt(
                1, answers.size(),
                "Please select the correct answer (enter the number):",
                "Invalid answer. Please enter a number between 1 and " + answers.size()
        );

        // Check if the selected answer is correct
        return answers.get(userAnswer - 1).isCorrect();
    }
}