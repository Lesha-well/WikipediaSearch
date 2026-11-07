
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

        ConsoleInput input = new ConsoleInput();
        ConsoleOutput output = new ConsoleOutput();

        WikipediaApiClient apiClient = new WikipediaApiClient();
        WikipediaResponseParser parser = new WikipediaResponseParser();

        WikipediaSearchService searchService =
                new WikipediaSearchService(apiClient, parser);

        BrowserOpener browserOpener = new BrowserOpener();

        output.showWelcome();

        try (input) {

            boolean running = true;

            while (running) {

                // Получаем поисковый запрос
                output.showSearchPrompt();

                String query = input.readLine();

                if (query == null || query.trim().equalsIgnoreCase("exit")) {
                    break;
                }

                query = query.trim();

                if (query.isEmpty()) {
                    output.showEmptyQuery();
                    continue;
                }

                // Выполняем поиск
                output.showSearching();

                List<Article> articles;

                try {
                    articles = searchService.search(query);

                } catch (WikipediaApiException e) {
                    output.showApiError(e.getMessage());
                    continue;

                } catch (WikipediaResponseException e) {
                    output.showResponseError(e.getMessage());
                    continue;

                } catch (IOException e) {
                    output.showNetworkError(e.getMessage());
                    continue;

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    output.showInterrupted();
                    break;
                }

                // Проверяем результаты поиска
                if (articles.isEmpty()) {
                    output.showNoResults();
                    continue;
                }

                output.showResults(articles);

                // Предлагаем выбрать статью
                boolean choosing = true;

                while (choosing) {

                    output.promptForArticleChoice();

                    String choice = input.readLine();

                    if (choice == null || choice.trim().equalsIgnoreCase("exit")) {
                        running = false;
                        break;
                    }

                    choice = choice.trim();

                    // 0 — начать новый поиск
                    if (choice.equals("0")) {
                        choosing = false;
                        continue;
                    }

                    OptionalInt selection =
                            input.parseArticleNumber(choice, articles.size());

                    if (selection.isEmpty()) {
                        output.showInvalidChoice();
                        continue;
                    }

                    // Получаем выбранную статью
                    Article selectedArticle =
                            articles.get(selection.getAsInt() - 1);

                    // Открываем статью в браузере
                    output.showOpeningArticle(selectedArticle);

                    try {
                        browserOpener.open(selectedArticle);

                    } catch (IOException e) {
                        output.showBrowserError(e.getMessage());
                    }

                    choosing = false;
                }
            }

        } catch (IOException e) {
            output.showInputError();
        }

        output.showGoodbye();
    }
}