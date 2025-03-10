package ru.otus.hw.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.hw.service.TestRunnerService;

@ShellComponent
@RequiredArgsConstructor
public class TestRunnerCommands {

    private final TestRunnerService testRunnerService;

    @ShellMethod(value = "Run the test", key = {"run-test", "test"})
    public void runTests() {
        testRunnerService.run();
    }
}
