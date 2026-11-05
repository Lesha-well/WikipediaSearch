package org.example.wikifinder;

import org.example.wikifinder.api.WikipediaApiClient;
import org.example.wikifinder.console.ConsoleInput;
import org.example.wikifinder.console.ConsoleOutput;
import org.example.wikifinder.model.Article;
import org.example.wikifinder.parser.WikipediaResponseParser;
import org.example.wikifinder.service.WikipediaSearchService;
import com.google.gson.JsonParseException;

import java.io.IOException;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        ConsoleInput input = new ConsoleInput();
        ConsoleOutput output = new ConsoleOutput();

        WikipediaApiClient apiClient = new WikipediaApiClient();
        WikipediaResponseParser parser = new WikipediaResponseParser();

        WikipediaSearchService searchService =
                new WikipediaSearchService(apiClient, parser);

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

                    List<Article> articles =
                            searchService.search(query.trim());

                    output.showResultCount(articles.size());

                } catch (IOException e) {

                    output.showNetworkError(e.getMessage());

                } catch (InterruptedException e) {

                    Thread.currentThread().interrupt();
                    output.showInterrupted();
                    break;

                } catch (JsonParseException | IllegalArgumentException e) {

                    output.showResponseError(e.getMessage());
                }
            }

        } catch (IOException e) {
            output.showInputError();
        }

        output.showGoodbye();
    }
}