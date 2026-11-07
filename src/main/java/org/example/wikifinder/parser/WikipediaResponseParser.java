
package org.example.wikifinder.parser;

import org.example.wikifinder.exception.WikipediaApiException;
import org.example.wikifinder.exception.WikipediaResponseException;
import org.example.wikifinder.model.Article;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonParseException;

import java.util.ArrayList;
import java.util.List;

public class WikipediaResponseParser {

    public List<Article> parse(String json)
            throws WikipediaApiException, WikipediaResponseException {

        if (json == null || json.isBlank()) {
            throw new WikipediaResponseException("Получен пустой ответ.");
        }

        JsonElement rootElement;

        try {
            rootElement = JsonParser.parseString(json);
        } catch (JsonParseException e) {
            throw new WikipediaResponseException(
                    "Не удалось разобрать JSON.", e
            );
        }

        if (!rootElement.isJsonObject()) {
            throw new WikipediaResponseException(
                    "Некорректная структура ответа Википедии."
            );
        }

        JsonObject root = rootElement.getAsJsonObject();

        // Проверяем ошибку API
        if (root.has("error")) {
            JsonElement errorElement = root.get("error");

            if (errorElement.isJsonObject()) {
                JsonObject error = errorElement.getAsJsonObject();

                String message = error.has("info")
                        ? error.get("info").getAsString()
                        : "Неизвестная ошибка API";

                throw new WikipediaApiException(message);
            }

            throw new WikipediaResponseException(
                    "Некорректный формат ошибки API."
            );
        }

        // Получаем query
        JsonElement queryElement = root.get("query");

        if (queryElement == null || !queryElement.isJsonObject()) {
            throw new WikipediaResponseException(
                    "В ответе отсутствует объект query."
            );
        }

        JsonObject queryObject = queryElement.getAsJsonObject();

        // Получаем search
        JsonElement searchElement = queryObject.get("search");

        if (searchElement == null || !searchElement.isJsonArray()) {
            throw new WikipediaResponseException(
                    "В ответе отсутствует массив search."
            );
        }

        JsonArray searchArray = searchElement.getAsJsonArray();

        List<Article> articles = new ArrayList<>();

        for (int i = 0; i < searchArray.size(); i++) {

            JsonElement element = searchArray.get(i);

            if (!element.isJsonObject()) {
                throw new WikipediaResponseException(
                        "Некорректный элемент поиска с индексом " + i
                );
            }

            JsonObject articleObject = element.getAsJsonObject();

            JsonElement pageIdElement = articleObject.get("pageid");
            JsonElement titleElement = articleObject.get("title");

            if (pageIdElement == null || !pageIdElement.isJsonPrimitive()
                    || !pageIdElement.getAsJsonPrimitive().isNumber()) {
                throw new WikipediaResponseException(
                        "Некорректный pageid у результата " + i
                );
            }

            if (titleElement == null || !titleElement.isJsonPrimitive()
                    || !titleElement.getAsJsonPrimitive().isString()) {
                throw new WikipediaResponseException(
                        "Некорректный title у результата " + i
                );
            }

            long pageId = pageIdElement.getAsLong();
            String title = titleElement.getAsString();

            String snippet = "";

            JsonElement snippetElement = articleObject.get("snippet");

            if (snippetElement != null && !snippetElement.isJsonNull()) {

                if (!snippetElement.isJsonPrimitive()
                        || !snippetElement.getAsJsonPrimitive().isString()) {
                    throw new WikipediaResponseException(
                            "Некорректный snippet у результата " + i
                    );
                }

                snippet = snippetElement.getAsString();
            }

            articles.add(new Article(pageId, title, snippet));
        }

        return articles;
    }
}