package ru.otus.hw.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class TestServiceImplTest {

    private IOService ioService;

    private QuestionDao questionDao;

    private LocalizationService localizationService;

    private TestServiceImpl testService;

    @BeforeEach
    void setUp() {
        ioService = mock(IOService.class);

        questionDao = mock(QuestionDao.class);

        localizationService = mock(LocalizationService.class);

        testService = new TestServiceImpl(ioService, questionDao, localizationService);
    }

    @Test
    void executeTestFor_withCorrectAnswers() {
        // Arrange
        Student student = new Student("Sergio", "Davidiants Misailidi");

        Question question1 = new Question("Question 1?", List.of(
                new Answer("Answer 1.1", false),
                new Answer("Answer 1.2", true)
        ));

        Question question2 = new Question("Question 2?", List.of(
                new Answer("Answer 2.1", true),
                new Answer("Answer 2.2", false)
        ));

        List<Question> questions = List.of(question1, question2);

        // Mock questionDao to return predefined questions
        when(questionDao.findAll()).thenReturn(questions);

        // Mock localized messages
        when(localizationService.getMessage("TestService.answer.the.questions"))
                .thenReturn("Answer the questions:");

        when(localizationService.getMessage("TestService.select.correct.answer"))
                .thenReturn("Select the correct answer:");

        when(localizationService.getMessage(eq("TestService.invalid.input"), any()))
                .thenReturn("Invalid input.");

        // Mock user input: the user selects the correct answers for both questions
        when(ioService.readIntForRangeWithPrompt(eq(1), eq(2), anyString(), anyString()))
                .thenReturn(2) // Correct answer for question 1
                .thenReturn(1); // Correct answer for question 2

        // Act
        TestResult result = testService.executeTestFor(student);

        // Assert
        assertNotNull(result);

        assertEquals(student, result.getStudent());

        assertEquals(2, result.getRightAnswersCount());

        // Verify the interactions with mocks
        verify(ioService, times(3)).printLine(""); // Ensure an empty line is printed

        verify(ioService).printLine("Answer the questions:");

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);

        verify(ioService, times(6)).printFormattedLine(captor.capture(), any(), any());

        List<String> capturedMessages = captor.getAllValues();

        // Assert the correct format of printFormattedLine calls
        assertEquals(List.of(
                "%d. %s", // First question formatting
                "   %d) %s", "   %d) %s", // First question's answers
                "%d. %s", // Second question formatting
                "   %d) %s", "   %d) %s"  // Second question's answers
        ), capturedMessages);
    }

    @Test
    void executeTestFor_withWrongAnswers() {
        // Arrange
        Student student = new Student("John", "Doe");

        Question question1 = new Question("Question 1?", List.of(
                new Answer("Answer 1.1", false),
                new Answer("Answer 1.2", true)
        ));

        List<Question> questions = List.of(question1);

        // Mock questionDao to return predefined questions
        when(questionDao.findAll()).thenReturn(questions);

        // Mock localized messages
        when(localizationService.getMessage("TestService.answer.the.questions")).thenReturn("Answer the questions:");
        when(localizationService.getMessage("TestService.select.correct.answer")).thenReturn("Select the correct answer:");
        when(localizationService.getMessage(eq("TestService.invalid.input"), any())).thenReturn("Invalid input.");

        // Mock user input: the user selects the wrong answer
        when(ioService.readIntForRangeWithPrompt(eq(1), eq(2), anyString(), anyString()))
                .thenReturn(1); // Wrong answer for question 1

        // Act
        TestResult result = testService.executeTestFor(student);

        // Assert
        assertNotNull(result);
        assertEquals(student, result.getStudent());
        assertEquals(0, result.getRightAnswersCount());
        assertEquals(questions, result.getAnsweredQuestions());
    }
}