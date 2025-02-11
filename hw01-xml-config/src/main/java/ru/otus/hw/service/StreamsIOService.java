package ru.otus.hw.service;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

/**
 * IOService implementation that writes output to a PrintStream.
 * This implementation supports both external PrintStream injection
 * and in-memory capturing using a ByteArrayOutputStream.
 */
public class StreamsIOService implements IOService {

    private final PrintStream printStream;

    private final ByteArrayOutputStream byteArrayOutputStream;

    /**
     * Default constructor that initializes the service with an in-memory output stream.
     */
    public StreamsIOService() {
        this.byteArrayOutputStream = new ByteArrayOutputStream();
        this.printStream = new PrintStream(byteArrayOutputStream, true, StandardCharsets.UTF_8);
    }

    /**
     * Constructor that allows injection of a custom PrintStream.
     *
     * @param printStream the PrintStream to use for output.
     */
    public StreamsIOService(PrintStream printStream) {
        this.printStream = printStream;
        this.byteArrayOutputStream = null; // No in-memory capturing when a custom stream is provided.
    }

    @Override
    public void printLine(String s) {
        printStream.println(s);
    }

    @Override
    public void printFormattedLine(String s, Object... args) {
        // Print the formatted string and then print a newline separately.
        printStream.printf(s, args);
        printStream.println();
    }

    /**
     * Retrieves the content written to the in-memory output stream.
     * This method is only available when the default constructor was used.
     *
     * @return the captured output as a String, or null if a custom PrintStream was used.
     */
    public String getOutput() {
        if (byteArrayOutputStream != null) {
            return byteArrayOutputStream.toString(StandardCharsets.UTF_8);
        }
        return null;
    }
}
