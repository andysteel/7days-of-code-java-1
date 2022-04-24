package com.gmail.andersoninfonet.services;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.concurrent.CompletableFuture;

import com.gmail.andersoninfonet.utils.LoadProperties;

/**
 * ImbdGetTopMoviesService
 * @since 0.1.0
 */
public class ImbdGetTopMoviesService implements ApiClient {

    private static final String BASE_URL = LoadProperties.getValue("api.imdb.base.url");

    public static ImbdGetTopMoviesService getInstance() {
        return new ImbdGetTopMoviesService();
    }

    @Override
    public CompletableFuture<String> getBody(String apiKey) {
        var apiTopMovies = new StringBuilder(BASE_URL);
        apiTopMovies
            .append("Top250Movies/")
            .append(apiKey);

        var httpClient = HttpClient
            .newBuilder()
            .build();
        
        HttpRequest httpRequest = null;
        
        try {
            httpRequest = HttpRequest
                .newBuilder(new URI(apiTopMovies.toString()))
                .GET()
                .build();

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return httpClient.sendAsync(httpRequest, BodyHandlers.ofString())
            .thenApply(HttpResponse::body)
            .toCompletableFuture();
    }
}