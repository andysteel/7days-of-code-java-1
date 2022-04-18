package com.gmail.andersoninfonet.services;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.concurrent.CompletableFuture;

/**
 * ImbdGetTopMoviesService
 * @since 0.1.0
 */
public class ImbdGetTopMoviesService {

    public static ImbdGetTopMoviesService getInstance() {
        return new ImbdGetTopMoviesService();
    }

    public CompletableFuture<String> getTopMovies(String baseUrl, String apikey) {
        var apiTopMovies = new StringBuilder(baseUrl);
        apiTopMovies
            .append("Top250Movies/")
            .append(apikey);

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