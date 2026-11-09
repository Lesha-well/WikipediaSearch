package org.example.wikifinder;

import org.example.wikifinder.api.WikipediaApiClient;
import org.example.wikifinder.browser.BrowserOpener;
import org.example.wikifinder.console.ConsoleInput;
import org.example.wikifinder.console.ConsoleOutput;
import org.example.wikifinder.exception.WikipediaApiException;
import org.example.wikifinder.exception.WikipediaResponseException;
import org.example.wikifinder.model.Article;
import org.example.wikifinder.parser.WikipediaResponseParser;
import org.example.wikifinder.service.WikipediaSearchService;

import java.io.IOException;
import java.util.List;
import java.util.OptionalInt;

public class Main {

    public static void main(String[] args) {

        WikipediaApiClient apiClient = new WikipediaApiClient();
        WikipediaResponseParser parser = new WikipediaResponseParser();

        WikipediaSearchService searchService =
                new WikipediaSearchService(apiClient, parser);

        BrowserOpener browserOpener = new BrowserOpener();

        ConsoleOutput.showWelcome();

        try {

            boolean running = true;

            while (running) {

                ConsoleOutput.showSearchPrompt();

                String query = ConsoleInput.readLine();

                if (query == null || query.trim().equalsIgnoreCase("exit")) {
                    break;
                }

                query = query.trim();

                if (query.isEmpty()) {
                    ConsoleOutput.showEmptyQuery();
                    continue;
                }

                ConsoleOutput.showSearching();

                List<Article> articles;

                try {

                    articles = searchService.search(query);

                } catch (WikipediaApiException e) {

                    ConsoleOutput.showApiError(e.getMessage());
                    continue;

                } catch (WikipediaResponseException e) {

                    ConsoleOutput.showResponseError(e.getMessage());
                    continue;

                } catch (IOException e) {

                    ConsoleOutput.showNetworkError(e.getMessage());
                    continue;

                } catch (InterruptedException e) {

                    Thread.currentThread().interrupt();
                    ConsoleOutput.showInterrupted();
                    break;
                }

                // Проверяем наличие результатов
                if (articles.isEmpty()) {
                    ConsoleOutput.showNoResults();
                    continue;
                }

                // Выводим результаты поиска
                ConsoleOutput.showResults(articles);

                // Обрабатываем выбор пользователя
                boolean choosing = true;

                while (choosing) {

                    ConsoleOutput.promptForArticleChoice();

                    String choice = ConsoleInput.readLine();

                    if (choice == null || choice.trim().equalsIgnoreCase("exit")) {
                        running = false;
                        break;
                    }

                    choice = choice.trim();

                    if (choice.equals("0")) {
                        choosing = false;
                        continue;
                    }

                    OptionalInt selection =
                            ConsoleInput.parseArticleNumber(
                                    choice,
                                    articles.size()
                            );

                    if (selection.isEmpty()) {
                        ConsoleOutput.showInvalidChoice();
                        continue;
                    }

                    Article selectedArticle =
                            articles.get(selection.getAsInt() - 1);

                    // Открываем выбранную статью
                    ConsoleOutput.showOpeningArticle(selectedArticle);

                    try {

                        browserOpener.open(selectedArticle);

                    } catch (IOException e) {

                        ConsoleOutput.showBrowserError(e.getMessage());
                    }

                    choosing = false;
                }
            }

        } catch (IOException e) {

            ConsoleOutput.showInputError();
        }

        ConsoleOutput.showGoodbye();
    }
}