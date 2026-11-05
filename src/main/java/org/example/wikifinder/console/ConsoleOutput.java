package org.example.wikifinder.console;

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

    public void showAcceptedQuery(String query) {
        System.out.println("Получен запрос: " + query);
    }

    public void showGoodbye() {
        System.out.println();
        System.out.println("Программа завершена.");
    }

    public void showInputError() {
        System.out.println("Ошибка при чтении пользовательского ввода.");
    }

    public void showSearching() {
        System.out.println();
        System.out.println("Выполняется поиск...");
    }

    public void showApiResponseReceived() {
        System.out.println("Получен ответ от Википедии.");
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
}