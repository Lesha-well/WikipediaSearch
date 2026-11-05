package org.example.wikifinder.console;

import org.example.wikifinder.model.Article;

import java.util.List;

public class ConsoleOutput {

    public void showWelcome() {
        System.out.println("========================================");
        System.out.println("       ПОИСКОВИК ПО ВИКИПЕДИИ");
        System.out.println("========================================");
    }

    public void showSearchPrompt() {
        System.out.println();
        System.out.println("Введите поисковый запрос (или exit для выхода):");
        System.out.print("> ");
    }

    public void showEmptyQuery() {
        System.out.println("Ошибка: поисковый запрос не может быть пустым.");
    }

    public void showSearching() {
        System.out.println();
        System.out.println("Выполняется поиск...");
    }

    public void showNetworkError(String message) {
        System.out.println("Ошибка при обращении к Википедии.");

        if (message != null && !message.isBlank()) {
            System.out.println("Подробности: " + message);
        }
    }

    public void showInterrupted() {
        System.out.println("Выполнение запроса было прервано.");
    }

    public void showResponseError(String message) {
        System.out.println("Ошибка при обработке ответа Википедии.");

        if (message != null && !message.isBlank()) {
            System.out.println("Подробности: " + message);
        }
    }

    public void showNoResults() {
        System.out.println("По вашему запросу ничего не найдено.");
    }

    public void showResults(List<Article> articles) {

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

    public void promptForArticleChoice() {
        System.out.println("Введите номер статьи для открытия.");
        System.out.println("Введите 0, чтобы выполнить новый поиск.");
        System.out.println("Введите exit для выхода.");
        System.out.print("> ");
    }

    public void showInvalidChoice() {
        System.out.println(
                "Некорректный выбор. Введите номер существующей статьи."
        );
    }

    public void showSelectedArticle(Article article) {
        System.out.println("Выбрана статья: " + article.getTitle());
    }

    public void showGoodbye() {
        System.out.println();
        System.out.println("Программа завершена.");
    }

    public void showInputError() {
        System.out.println("Ошибка при чтении пользовательского ввода.");
    }

    // Удаляем HTML-теги из описания, полученного от Википедии
    private String cleanSnippet(String snippet) {

        return snippet
                .replaceAll("<[^>]*>", "")
                .replace("&quot;", "\"")
                .replace("&amp;", "&")
                .replace("&lt;", "<")
                .replace("&gt;", ">")
                .replace("&#039;", "'");
    }

    public void showOpeningArticle(Article article) {
        System.out.println();
        System.out.println("Открываю статью: " + article.getTitle());
    }

    public void showBrowserError(String message) {
        System.out.println("Не удалось открыть статью в браузере.");

        if (message != null && !message.isBlank()) {
            System.out.println("Подробности: " + message);
        }
    }
}