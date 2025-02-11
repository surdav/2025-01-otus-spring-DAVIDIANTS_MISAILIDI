package ru.otus.hw.dao;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.dto.QuestionDto;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
public class CsvQuestionDao implements QuestionDao {

    private final TestFileNameProvider fileNameProvider;

    @Override
    public List<Question> findAll() {
        // Get the CSV file name (hardcoded in the XML via AppProperties)
        String filename = fileNameProvider.getTestFileName();
        try (Reader r = new InputStreamReader(
                Objects.requireNonNull(getResourceAsStream("/" + filename), "Resource not found"),
                StandardCharsets.UTF_8)) {
            // Build CsvToBean to parse QuestionDto objects
            CsvToBean<QuestionDto> csvToBean = new CsvToBeanBuilder<QuestionDto>(r)
                    .withType(QuestionDto.class)
                    // The CSV file separates question and answers with a semicolon
                    .withSeparator(';')
                    // Skip the header/comment line
                    .withSkipLines(1)
                    .build();
            List<QuestionDto> questionDtoList = csvToBean.parse();
            List<Question> questions = new ArrayList<>();
            for (QuestionDto dto : questionDtoList) {
                questions.add(dto.toDomainObject());
            }
            return questions;
        } catch (Exception e) {
            throw new QuestionReadException("Failed to read questions from CSV", e);
        }
    }

    /**
     * Protected method to allow stubbing in unit tests.
     */
    protected InputStream getResourceAsStream(String resourceName) {
        return getClass().getResourceAsStream(resourceName);
    }
}
