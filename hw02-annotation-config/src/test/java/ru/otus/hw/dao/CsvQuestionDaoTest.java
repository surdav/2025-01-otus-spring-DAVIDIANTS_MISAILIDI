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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class CsvQuestionDaoTest {

    private TestFileNameProvider testFileNameProvider;
    private CsvQuestionDao csvQuestionDao;

    @BeforeEach
    void setUp() {
        // Mocking the TestFileNameProvider dependency
        testFileNameProvider = mock(TestFileNameProvider.class);

        // Creating an instance of CsvQuestionDao
        csvQuestionDao = new CsvQuestionDao(testFileNameProvider);
    }

    @Test
    void findAll_ShouldReturnListOfQuestions_WhenCsvIsValid() {
        // Arrange
        String testFileName = "test-questions.csv";
        String csvContent = """
                # Header to skip
                What is 2+2?;4%true|3%false
                What is the capital of France?;Paris%true|London%false
                """;

        // Mocking the behavior of TestFileNameProvider
        when(testFileNameProvider.getTestFileName()).thenReturn(testFileName);

        // Simulating InputStream for the CSV content
        InputStream inputStream = new ByteArrayInputStream(csvContent.getBytes(StandardCharsets.UTF_8));

        // Mocking the behavior of ClassLoader and returning the InputStream
        ClassLoader classLoader = mock(ClassLoader.class);
        when(classLoader.getResourceAsStream(testFileName)).thenReturn(inputStream);

        // Injecting a mocked InputStream into the CsvQuestionDao logic
        doReturn(inputStream).when(classLoader).getResourceAsStream(testFileName);

        // Act
        List<Question> questions = csvQuestionDao.findAll();

        // Assert
        assertThat(questions).hasSize(3);
        assertThat(questions.get(0).text()).isEqualTo("Is there life on Mars?");
        assertThat(questions.get(1).text()).isEqualTo("How should resources be loaded form jar in Java?");
        assertThat(questions.get(2).text()).isEqualTo("Which option is a good way to handle the exception?");

        verify(testFileNameProvider, times(1)).getTestFileName();
    }

    @Test
    void findAll_ShouldThrowQuestionReadException_WhenResourceNotFound() {
        // Arrange
        String testFileName = "nonexistent.csv";
        when(testFileNameProvider.getTestFileName()).thenReturn(testFileName);

        // Simulating a missing resource (null)
        ClassLoader classLoader = mock(ClassLoader.class);
        when(classLoader.getResourceAsStream(testFileName)).thenReturn(null);

        // Act & Assert
        assertThatThrownBy(() -> csvQuestionDao.findAll())
                .isInstanceOf(QuestionReadException.class)
                .hasMessageContaining("Failed to read questions from CSV");

        verify(testFileNameProvider, times(1)).getTestFileName();
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

        // Simulating InputStream for malformed CSV content
        InputStream malformedCsvStream = new ByteArrayInputStream(csvContent.getBytes(StandardCharsets.UTF_8));

        // Mocking the behavior of ClassLoader
        ClassLoader classLoader = mock(ClassLoader.class);
        when(classLoader.getResourceAsStream(testFileName)).thenReturn(malformedCsvStream);

        // Act & Assert
        assertThatThrownBy(() -> csvQuestionDao.findAll())
                .isInstanceOf(QuestionReadException.class)
                .hasMessageContaining("Failed to read questions from CSV");

        // Verifying that fileNameProvider is called only once
        verify(testFileNameProvider, times(1)).getTestFileName();
    }
}