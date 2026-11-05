package org.example.wikifinder;

import org.example.wikifinder.api.WikipediaApiClient;
import org.example.wikifinder.console.ConsoleInput;
import org.example.wikifinder.console.ConsoleOutput;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {

        ConsoleInput input = new ConsoleInput();
        ConsoleOutput output = new ConsoleOutput();
        WikipediaApiClient apiClient = new WikipediaApiClient();

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

                try {

                    output.showSearching();

                    String response = apiClient.search(query.trim());

                    if (response != null && !response.isBlank()) {
                        output.showApiResponseReceived();
                    }

                } catch (IOException e) {

                    output.showNetworkError(e.getMessage());

                } catch (InterruptedException e) {

                    Thread.currentThread().interrupt();
                    output.showInterrupted();
                    break;
                }
            }

        } catch (IOException e) {
            output.showInputError();
        }

        output.showGoodbye();
    }
}