package org.example.wikifinder;

import org.example.wikifinder.api.WikipediaApiClient;
import org.example.wikifinder.browser.BrowserOpener;
import org.example.wikifinder.console.ConsoleInput;
import org.example.wikifinder.console.ConsoleOutput;
import org.example.wikifinder.model.Article;
import org.example.wikifinder.parser.WikipediaResponseParser;
import org.example.wikifinder.service.WikipediaSearchService;
import com.google.gson.JsonParseException;

import java.io.IOException;
import java.util.List;
import java.util.OptionalInt;

public class Main {

    public static void main(String[] args) {

        ConsoleInput input = new ConsoleInput();
        ConsoleOutput output = new ConsoleOutput();

        WikipediaApiClient apiClient = new WikipediaApiClient();
        WikipediaResponseParser parser = new WikipediaResponseParser();

        WikipediaSearchService searchService =
                new WikipediaSearchService(apiClient, parser);

        BrowserOpener browserOpener = new BrowserOpener();

        output.showWelcome();

        try (input) {

            boolean shouldExit = false;

            while (!shouldExit) {

                output.showSearchPrompt();

                String query = input.readLine();

                if (query == null || query.trim().equalsIgnoreCase("exit")) {
                    break;
                }

                if (query.isBlank()) {
                    output.showEmptyQuery();
                    continue;
                }

                List<Article> articles;

                try {

                    output.showSearching();

                    articles = searchService.search(query.trim());

                } catch (IOException e) {

                    output.showNetworkError(e.getMessage());
                    continue;

                } catch (InterruptedException e) {

                    Thread.currentThread().interrupt();
                    output.showInterrupted();
                    break;

                } catch (JsonParseException | IllegalArgumentException e) {

                    output.showResponseError(e.getMessage());
                    continue;
                }

                // Проверяем наличие результатов
                if (articles.isEmpty()) {
                    output.showNoResults();
                    continue;
                }

                // Выводим результаты поиска
                output.showResults(articles);

                // Обрабатываем выбор пользователя
                while (true) {

                    output.promptForArticleChoice();

                    String choice = input.readLine();

                    if (choice == null || choice.trim().equalsIgnoreCase("exit")) {
                        shouldExit = true;
                        break;
                    }

                    if (choice.trim().equals("0")) {
                        break;
                    }

                    OptionalInt selectedNumber =
                            input.parseArticleNumber(choice, articles.size());

                    if (selectedNumber.isEmpty()) {
                        output.showInvalidChoice();
                        continue;
                    }

                    Article selectedArticle =
                            articles.get(selectedNumber.getAsInt() - 1);

                    // Открываем выбранную статью
                    try {

                        output.showOpeningArticle(selectedArticle);

                        browserOpener.open(selectedArticle);

                    } catch (IOException e) {

                        output.showBrowserError(e.getMessage());
                    }

                    break;
                }
            }

        } catch (IOException e) {
            output.showInputError();
        }

        output.showGoodbye();
    }
}