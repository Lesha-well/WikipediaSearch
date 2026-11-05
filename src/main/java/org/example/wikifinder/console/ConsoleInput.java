package org.example.wikifinder.console;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.OptionalInt;

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

    public OptionalInt parseArticleNumber(String value, int maxNumber) {

        try {

            int number = Integer.parseInt(value.trim());

            if (number < 1 || number > maxNumber) {
                return OptionalInt.empty();
            }

            return OptionalInt.of(number);

        } catch (NumberFormatException e) {

            return OptionalInt.empty();
        }
    }

    @Override
    public void close() throws IOException {
        reader.close();
    }
}