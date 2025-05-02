package ru.otus.hw.commands;

import lombok.RequiredArgsConstructor;
import org.h2.tools.Console;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@RequiredArgsConstructor
@ShellComponent
public class LibraryCommands {

    @ShellMethod(value = "Start H2 Console", key = "h2")
    public void startH2Console() {
        try {
            Console.main();
        } catch (Exception e) {
            System.out.println("Failed to start H2 Console: " + e.getMessage());
        }
    }
}
