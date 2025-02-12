package ru.otus.hw.service;

import org.junit.jupiter.api.Test;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class TestServiceImplTest {

    // A simple IOService implementation that collects printed output
    static class TestIOService implements IOService {

        private final StringBuilder sb = new StringBuilder();

        @Override
        public void printLine(String s) {
            sb.append(s).append("\n");
        }

        @Override
        public void printFormattedLine(String s, Object... args) {
            sb.append(String.format(s, args)).append("\n");
        }

        public String getOutput() {
            return sb.toString();
        }
    }

    @Test
    void testExecuteTest() {
        // Prepare sample questions
        QuestionDao dummyDao = getQuestionDao();

        TestIOService testIOService = new TestIOService();
        TestServiceImpl testService = new TestServiceImpl(testIOService, dummyDao);

        testService.executeTest();

        String output = testIOService.getOutput();

        // Assert that the output contains the expected questions and answers
        assertThat(output)
                .contains("1. What is the capital of France?")
                .contains("1) Paris")
                .contains("2) London")
                .contains("2. What is 2+2?")
                .contains("1) 4");
    }

    private static QuestionDao getQuestionDao() {
        List<Question> questions = List.of(
                new Question(
                        "What is the capital of France?", List.of(
                                new Answer("Paris", true),
                                new Answer("London", false)
                )),
                new Question(
                        "What is 2+2?",
                        Collections.singletonList(
                                new Answer("4", true)
                        ))
        );

        // Create a dummy QuestionDao returning our sample questions
        return () -> questions;
    }

}