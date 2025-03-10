package ru.otus.hw.service;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@SpringBootTest
@ContextConfiguration(classes = {TestServiceImpl.class, IOService.class, QuestionDao.class, LocalizationService.class})
class TestServiceImplTest {

    @MockitoBean
    private IOService ioService;

    @MockitoBean
    private QuestionDao questionDao;

    @MockitoBean
    private LocalizationService localizationService;

    @Autowired
    private TestServiceImpl testService;

    @Test
    void executeTestFor_WhenCorrectAnswers() {
        // Arrange
        Student student = new Student("John", "Doe");
        Question question = new Question("What is 2+2?", List.of(
                new Answer("3", false),
                new Answer("4", true)
        ));

        when(questionDao.findAll()).thenReturn(List.of(question));
        when(localizationService.getMessage(Mockito.any())).thenReturn("Answer the question:");
        when(ioService.readIntForRangeWithPrompt(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString(), Mockito.any()))
                .thenReturn(2);

        // Act
        TestResult result = testService.executeTestFor(student);

        // Assert
        assertEquals(1, result.getRightAnswersCount());
    }
}