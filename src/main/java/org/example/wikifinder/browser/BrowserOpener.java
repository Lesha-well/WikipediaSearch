package org.example.wikifinder.browser;

import org.example.wikifinder.model.Article;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;

public class BrowserOpener {

    private static final String ARTICLE_URL =
            "https://ru.wikipedia.org/w/index.php?curid=";

    public void open(Article article) throws IOException {

        // Проверяем возможность работы с браузером
        if (!Desktop.isDesktopSupported()) {
            throw new IOException(
                    "На данном устройстве не поддерживается открытие браузера."
            );
        }

        try {

            Desktop desktop = Desktop.getDesktop();

            // Проверяем поддержку действия BROWSE
            if (!desktop.isSupported(Desktop.Action.BROWSE)) {
                throw new IOException(
                        "Система не поддерживает открытие веб-страниц."
                );
            }

            // Формируем ссылку на статью
            String url = ARTICLE_URL + article.getPageId();

            URI uri = URI.create(url);

            // Открываем страницу
            desktop.browse(uri);

        } catch (UnsupportedOperationException | SecurityException e) {

            throw new IOException(
                    "Не удалось открыть браузер.",
                    e
            );
        }
    }
}