package org.example.wikifinder.console;

import org.example.wikifinder.model.Article;

import java.util.List;

public final class ConsoleOutput {

    private ConsoleOutput() {
    }

    public static void showWelcome() {
        System.out.println("========================================");
        System.out.println("       ПОИСКОВИК ПО ВИКИПЕДИИ");
        System.out.println("========================================");
    }

    public static void showSearchPrompt() {
        System.out.println();
        System.out.println("Введите поисковый запрос (или exit для выхода):");
        System.out.print("> ");
    }

    public static void showEmptyQuery() {
        System.out.println("Ошибка: поисковый запрос не может быть пустым.");
    }

    public static void showSearching() {
        System.out.println();
        System.out.println("Выполняется поиск...");
    }

    public static void showNetworkError(String message) {
        System.out.println("Ошибка при обращении к Википедии.");

        if (message != null && !message.isBlank()) {
            System.out.println("Подробности: " + message);
        }
    }

    public static void showApiError(String message) {
        System.out.println("Ошибка API Википедии.");

        if (message != null && !message.isBlank()) {
            System.out.println("Подробности: " + message);
        }
    }

    public static void showInterrupted() {
        System.out.println("Выполнение запроса было прервано.");
    }

    public static void showResponseError(String message) {
        System.out.println("Ошибка при обработке ответа Википедии.");

        if (message != null && !message.isBlank()) {
            System.out.println("Подробности: " + message);
        }
    }

    public static void showNoResults() {
        System.out.println("По вашему запросу ничего не найдено.");
    }

    public static void showResults(List<Article> articles) {

        System.out.println();
        System.out.println("Найденные статьи:");

        for (int i = 0; i < articles.size(); i++) {

            Article article = articles.get(i);

            System.out.println();
            System.out.println((i + 1) + ". " + article.getTitle());

            if (!article.getSnippet().isBlank()) {
                System.out.println("   " + cleanSnippet(article.getSnippet()));
            }
        }

        System.out.println();
    }

    public static void promptForArticleChoice() {
        System.out.println("Введите номер статьи для открытия.");
        System.out.println("Введите 0, чтобы выполнить новый поиск.");
        System.out.println("Введите exit для выхода.");
        System.out.print("> ");
    }

    public static void showInvalidChoice() {
        System.out.println(
                "Некорректный выбор. Введите номер существующей статьи."
        );
    }

    public static void showGoodbye() {
        System.out.println();
        System.out.println("Программа завершена.");
    }

    public static void showInputError() {
        System.out.println("Ошибка при чтении пользовательского ввода.");
    }

    // Удаляем HTML-теги из описания, полученного от Википедии
    private static String cleanSnippet(String snippet) {

        return snippet
                .replaceAll("<[^>]*>", "")
                .replace("&quot;", "\"")
                .replace("&amp;", "&")
                .replace("&lt;", "<")
                .replace("&gt;", ">")
                .replace("&#039;", "'");
    }

    public static void showOpeningArticle(Article article) {
        System.out.println();
        System.out.println("Открываю статью: " + article.getTitle());
    }

    public static void showBrowserError(String message) {
        System.out.println("Не удалось открыть статью в браузере.");

        if (message != null && !message.isBlank()) {
            System.out.println("Подробности: " + message);
        }
    }
}