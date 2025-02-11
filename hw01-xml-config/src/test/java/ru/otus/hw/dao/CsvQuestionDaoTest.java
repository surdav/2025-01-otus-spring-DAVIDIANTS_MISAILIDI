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
    private CsvQuestionDao csvQuestionDaoSpy;

    @BeforeEach
    void setUp() {
        // Mock the TestFileNameProvider dependency
        testFileNameProvider = mock(TestFileNameProvider.class);
        // Create a spy of CsvQuestionDao so we can stub getResourceAsStream()
        CsvQuestionDao dao = new CsvQuestionDao(testFileNameProvider);
        csvQuestionDaoSpy = spy(dao);
    }

    @Test
    void findAll_ShouldReturnListOfQuestions_WhenCsvIsValid() {
        // Arrange
        String testFileName = "test-questions.csv";
        // Example CSV content matching your expected format:
        // The first line is a header (skipped)
        // Then one line per question:
        // Format: <question>;<answer1>%<flag>|<answer2>%<flag>|...
        String csvContent = """
                # Header to skip
                What is 2+2?;4%true|3%false
                What is the capital of France?;Paris%true|London%false
                """;

        when(testFileNameProvider.getTestFileName()).thenReturn(testFileName);
        InputStream csvStream = new ByteArrayInputStream(csvContent.getBytes(StandardCharsets.UTF_8));

        // Stub the protected getResourceAsStream method using our spy
        doReturn(csvStream).when(csvQuestionDaoSpy).getResourceAsStream("/" + testFileName);

        // Act
        List<Question> questions = csvQuestionDaoSpy.findAll();

        // Assert
        assertThat(questions).hasSize(2);
        assertThat(questions.get(0).text()).isEqualTo("What is 2+2?");
        assertThat(questions.get(1).text()).isEqualTo("What is the capital of France?");
    }

    @Test
    void findAll_ShouldThrowQuestionReadException_WhenResourceNotFound() {
        // Arrange
        String testFileName = "nonexistent.csv";
        when(testFileNameProvider.getTestFileName()).thenReturn(testFileName);
        // Stub the method to return null, simulating resource not found
        doReturn(null).when(csvQuestionDaoSpy).getResourceAsStream("/" + testFileName);

        // Act & Assert
        assertThatThrownBy(csvQuestionDaoSpy::findAll)
                .isInstanceOf(QuestionReadException.class)
                .hasMessageContaining("Failed to read questions from CSV");
    }

    @Test
    void findAll_ShouldThrowQuestionReadException_WhenCsvIsMalformed() {
        // Arrange
        String testFileName = "malformed-questions.csv";
        // CSV content with an unexpected format (missing '%' separator)
        String csvContent = """
             # Header to skip
             Only one column here;MalformedAnswer
             """;

        when(testFileNameProvider.getTestFileName()).thenReturn(testFileName);
        InputStream csvStream = new ByteArrayInputStream(csvContent.getBytes(StandardCharsets.UTF_8));
        doReturn(csvStream).when(csvQuestionDaoSpy).getResourceAsStream("/" + testFileName);

        // Act & Assert
        assertThatThrownBy(csvQuestionDaoSpy::findAll)
                .isInstanceOf(QuestionReadException.class)
                .hasMessageContaining("Failed to read questions from CSV");

        verify(testFileNameProvider, times(1)).getTestFileName();
    }
}