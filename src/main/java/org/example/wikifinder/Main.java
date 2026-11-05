package org.example.wikifinder;

import org.example.wikifinder.console.ConsoleInput;
import org.example.wikifinder.console.ConsoleOutput;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {

        ConsoleInput input = new ConsoleInput();
        ConsoleOutput output = new ConsoleOutput();

        output.showWelcome();

        try (input) {

            while (true) {

                output.showSearchPrompt();

                String query = input.readLine();

                if (query == null || query.trim().equalsIgnoreCase("exit")) {
                    break;
                }

                if (query.isBlank()) {
                    output.showEmptyQuery();
                    continue;
                }

                output.showAcceptedQuery(query.trim());
            }

        } catch (IOException e) {
            output.showInputError();
        }

        output.showGoodbye();
    }
}