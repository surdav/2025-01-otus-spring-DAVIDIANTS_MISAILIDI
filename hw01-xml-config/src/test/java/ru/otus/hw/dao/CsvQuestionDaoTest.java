package ru.otus.hw.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class CsvQuestionDaoTest {

    private TestFileNameProvider testFileNameProvider;
    private CsvQuestionDao csvQuestionDao;

    @BeforeEach
    void setUp() {
        // Mock the TestFileNameProvider dependency
        testFileNameProvider = mock(TestFileNameProvider.class);

        // Create a new instance of CsvQuestionDao
        csvQuestionDao = new CsvQuestionDao(testFileNameProvider);
    }

    @Test
    void findAll_ShouldReturnListOfQuestions_WhenCsvIsValid() {
        // Arrange
        String testFileName = "test-questions.csv";
        when(testFileNameProvider.getTestFileName()).thenReturn(testFileName);

        // Act
        List<Question> questions = csvQuestionDao.findAll();

        // Assert
        assertThat(questions).hasSize(3);
        assertThat(questions.get(0).text()).isEqualTo("Is there life on Mars?");
        assertThat(questions.get(1).text()).isEqualTo("How should resources be loaded form jar in Java?");
        assertThat(questions.get(2).text()).isEqualTo("Which option is a good way to handle the exception?");
    }

    @Test
    void findAll_ShouldThrowQuestionReadException_WhenResourceNotFound() {
        // Arrange
        String testFileName = "nonexistent.csv";
        when(testFileNameProvider.getTestFileName()).thenReturn(testFileName);

        // Simulate returning null to replicate "resource not found"
        ClassLoader classLoaderMock = mock(ClassLoader.class);
        when(classLoaderMock.getResourceAsStream(testFileName)).thenReturn(null);
        Thread.currentThread().setContextClassLoader(classLoaderMock);

        // Act & Assert
        assertThatThrownBy(csvQuestionDao::findAll)
                .isInstanceOf(QuestionReadException.class)
                .hasMessageContaining("Failed to read questions from CSV");
    }

    @Test
    void findAll_ShouldThrowQuestionReadException_WhenCsvIsMalformed() {
        // Arrange
        String testFileName = "malformed-test-questions.csv";
        String csvContent = """
                # Header to skip
                Only one column here;MalformedAnswer
                """;

        when(testFileNameProvider.getTestFileName()).thenReturn(testFileName);

        // Simulate InputStream for the malformed CSV content
        InputStream malformedCsvStream = new ByteArrayInputStream(csvContent.getBytes(StandardCharsets.UTF_8));

        // Mock the behavior of ClassLoader
        ClassLoader classLoader = mock(ClassLoader.class);
        when(classLoader.getResourceAsStream(testFileName)).thenReturn(malformedCsvStream);
        Thread.currentThread().setContextClassLoader(classLoader);

        // Act & Assert
        assertThatThrownBy(csvQuestionDao::findAll)
                .isInstanceOf(QuestionReadException.class)
                .hasMessageContaining("Failed to read questions from CSV");

        verify(testFileNameProvider, times(1)).getTestFileName();
    }
}