package org.example.wikifinder.console;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.OptionalInt;

public final class ConsoleInput {

    private static final BufferedReader READER =
            new BufferedReader(
                    new InputStreamReader(System.in, StandardCharsets.UTF_8)
            );

    private ConsoleInput() {
    }

    public static String readLine() throws IOException {
        return READER.readLine();
    }

    public static OptionalInt parseArticleNumber(String value, int maxNumber) {

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
}