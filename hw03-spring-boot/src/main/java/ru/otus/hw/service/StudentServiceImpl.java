package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import ru.otus.hw.domain.Student;

@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final IOService ioService;

    private final LocalizationService localizationService;

    @Override
    public Student determineCurrentStudent() {
        var firstName = ioService.readStringWithPrompt(
                localizationService.getMessage("StudentService.input.first.name")
        );

        var lastName = ioService.readStringWithPrompt(
                localizationService.getMessage("StudentService.input.last.name")
        );

        return new Student(firstName, lastName);
    }
}