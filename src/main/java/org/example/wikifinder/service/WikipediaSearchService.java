
package org.example.wikifinder.service;

import org.example.wikifinder.api.WikipediaApiClient;
import org.example.wikifinder.exception.WikipediaApiException;
import org.example.wikifinder.exception.WikipediaResponseException;
import org.example.wikifinder.model.Article;
import org.example.wikifinder.parser.WikipediaResponseParser;

import java.io.IOException;
import java.util.List;

public class WikipediaSearchService {

    private final WikipediaApiClient apiClient;
    private final WikipediaResponseParser parser;

    public WikipediaSearchService(
            WikipediaApiClient apiClient,
            WikipediaResponseParser parser
    ) {
        this.apiClient = apiClient;
        this.parser = parser;
    }

    public List<Article> search(String query)
            throws IOException, InterruptedException,
            WikipediaApiException, WikipediaResponseException {

        // Получаем JSON от Википедии
        String response = apiClient.search(query);

        // Преобразуем JSON в список статей
        return parser.parse(response);
    }
}
