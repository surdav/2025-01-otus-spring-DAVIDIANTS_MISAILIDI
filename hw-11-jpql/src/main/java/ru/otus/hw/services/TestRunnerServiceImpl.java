package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.commands.BookCommands;

@RequiredArgsConstructor
@Service
public class TestRunnerServiceImpl implements TestRunnerService {

    private final BookCommands commands;

    @Override
    public void run() {
        commands.findAllBooks();
    }
}
