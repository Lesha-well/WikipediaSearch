package org.example.wikifinder.api;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

public class WikipediaApiClient {

    private static final String API_URL =
            "https://ru.wikipedia.org/w/api.php";

    private final HttpClient httpClient;

    public WikipediaApiClient() {

        httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
    }

    public String search(String query)
            throws IOException, InterruptedException {

        String encodedQuery = URLEncoder.encode(
                query,
                StandardCharsets.UTF_8
        );

        String url = API_URL
                + "?action=query&list=search&utf8=1&format=json&srlimit=10&srsearch="
                + encodedQuery;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofSeconds(20))
                .header(
                        "User-Agent",
                        "WikipediaConsoleSearch/1.0 (Java 17 educational project)"
                )
                .header("Accept", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(
                request,
                HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8)
        );

        if (response.statusCode() != HttpURLConnection.HTTP_OK) {
            throw new IOException(
                    "Википедия вернула HTTP-статус: "
                            + response.statusCode()
            );
        }

        return response.body();
    }
}