package org.example.wikifinder.console;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class ConsoleInput implements AutoCloseable {

    private final BufferedReader reader;

    public ConsoleInput() {
        reader = new BufferedReader(
                new InputStreamReader(System.in, StandardCharsets.UTF_8)
        );
    }

    public String readLine() throws IOException {
        return reader.readLine();
    }

    @Override
    public void close() throws IOException {
        reader.close();
    }
}