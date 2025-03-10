package ru.otus.hw.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@SpringBootTest
@ContextConfiguration(classes = {CsvQuestionDao.class, TestFileNameProvider.class})
class CsvQuestionDaoTest {

    @MockitoBean
    private TestFileNameProvider testFileNameProvider;

    @Autowired
    private CsvQuestionDao csvQuestionDao;

    @Test
    void findAll_ShouldReturnListOfQuestions_WhenCsvIsValid() {
        // Arrange
        String testFileName = "test-questions.csv";
        when(testFileNameProvider.getFileName()).thenReturn(testFileName);

        // Act
        List<Question> questions = csvQuestionDao.findAll();

        // Assert
        assertThat(questions).isNotEmpty();
    }

    @Test
    void findAll_ShouldThrowQuestionReadException_WhenResourceNotFound() {
        // Arrange
        String invalidFileName = "invalid.csv";
        when(testFileNameProvider.getFileName()).thenReturn(invalidFileName);

        // Act & Assert
        assertThatThrownBy(() -> csvQuestionDao.findAll())
                .isInstanceOf(QuestionReadException.class)
                .hasMessageContaining("Failed to read questions from CSV");
    }
}