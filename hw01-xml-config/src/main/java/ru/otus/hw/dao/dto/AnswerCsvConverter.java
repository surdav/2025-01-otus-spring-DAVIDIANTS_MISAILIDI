package ru.otus.hw.dao.dto;

import com.opencsv.bean.AbstractCsvConverter;
import ru.otus.hw.domain.Answer;

public class AnswerCsvConverter extends AbstractCsvConverter {

    @Override
    public Object convertToRead(String value) {
        var valueArr = value.split("%");
        if (valueArr.length < 2) {
            throw new IllegalArgumentException("Malformed answer data: " + value);
        }
        return new Answer(valueArr[0], Boolean.parseBoolean(valueArr[1]));
    }
}