package org.example.wikifinder.parser;

import org.example.wikifinder.model.Article;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

public class WikipediaResponseParser {

    public List<Article> parse(String json) {

        if (json == null || json.isBlank()) {
            throw new IllegalArgumentException("Получен пустой ответ.");
        }

        // Преобразуем JSON в объект
        JsonElement rootElement = JsonParser.parseString(json);

        if (!rootElement.isJsonObject()) {
            throw new IllegalArgumentException(
                    "Некорректная структура ответа Википедии."
            );
        }

        JsonObject root = rootElement.getAsJsonObject();

        // Проверяем, не вернул ли API ошибку
        if (root.has("error") && root.get("error").isJsonObject()) {

            JsonObject error = root.getAsJsonObject("error");

            String message = error.has("info")
                    ? error.get("info").getAsString()
                    : "Неизвестная ошибка API";

            throw new IllegalArgumentException(
                    "Ошибка API Википедии: " + message
            );
        }

        // Получаем объект query
        JsonElement queryElement = root.get("query");

        if (queryElement == null || !queryElement.isJsonObject()) {
            return new ArrayList<>();
        }

        JsonObject queryObject = queryElement.getAsJsonObject();

        // Получаем массив search
        JsonElement searchElement = queryObject.get("search");

        if (searchElement == null || !searchElement.isJsonArray()) {
            return new ArrayList<>();
        }

        JsonArray searchArray = searchElement.getAsJsonArray();

        List<Article> articles = new ArrayList<>();

        for (JsonElement element : searchArray) {

            if (!element.isJsonObject()) {
                continue;
            }

            JsonObject articleObject = element.getAsJsonObject();

            JsonElement pageIdElement = articleObject.get("pageid");
            JsonElement titleElement = articleObject.get("title");

            // Проверяем наличие обязательных полей
            if (pageIdElement == null || titleElement == null
                    || pageIdElement.isJsonNull() || titleElement.isJsonNull()) {
                continue;
            }

            try {

                long pageId = pageIdElement.getAsLong();
                String title = titleElement.getAsString();

                String snippet = "";

                JsonElement snippetElement = articleObject.get("snippet");

                if (snippetElement != null && !snippetElement.isJsonNull()) {
                    snippet = snippetElement.getAsString();
                }

                articles.add(new Article(pageId, title, snippet));

            } catch (RuntimeException e) {
                // Пропускаем некорректный отдельный результат
            }
        }

        return articles;
    }
}