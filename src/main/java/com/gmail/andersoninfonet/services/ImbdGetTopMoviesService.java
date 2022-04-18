package com.gmail.andersoninfonet.services;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

/**
 * ImbdGetTopMoviesService
 * @since 0.1.0
 */
public class ImbdGetTopMoviesService {

    public static ImbdGetTopMoviesService getInstance() {
        return new ImbdGetTopMoviesService();
    }

    public void getTopMovies(String baseUrl, String apikey) {
        var apiTopMovies = new StringBuilder(baseUrl);
        apiTopMovies
            .append("Top250Movies/")
            .append(apikey);
        
        try {
            var httpRequest = HttpRequest
                .newBuilder(new URI(apiTopMovies.toString()))
                .GET()
                .build();

            var httpClient = HttpClient
                .newBuilder()
                .build();

            httpClient.sendAsync(httpRequest, BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(System.out::println)
                .join();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

    }
}