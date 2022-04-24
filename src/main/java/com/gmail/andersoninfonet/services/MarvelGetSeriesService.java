package com.gmail.andersoninfonet.services;

import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalTime;
import java.util.concurrent.CompletableFuture;

import com.gmail.andersoninfonet.utils.LoadProperties;

public class MarvelGetSeriesService implements ApiClient {

    private static final String BASE_URL = LoadProperties.getValue("api.marvel.base.url");
    private static final String PRIVATE_KEY = LoadProperties.getValue("marvel.private.key");

    public static MarvelGetSeriesService getInstance() {
        return new MarvelGetSeriesService();
    }
    @Override
    public CompletableFuture<String> getBody(String apiKey) {
        var apiSeriesMarvel = new StringBuilder(BASE_URL);
        var ts = String.valueOf(LocalTime.now().getNano());
        String marvelHash = generateHash(apiKey, ts, PRIVATE_KEY);

        apiSeriesMarvel
            .append("series?ts=")
            .append(ts)
            .append("&apikey=")
            .append(apiKey)
            .append("&hash=")
            .append(marvelHash);

        var httpClient = HttpClient
            .newBuilder()
            .build();
        
        HttpRequest httpRequest = null;
        
        try {
            httpRequest = HttpRequest
                .newBuilder(new URI(apiSeriesMarvel.toString()))
                .GET()
                .build();

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return httpClient.sendAsync(httpRequest, BodyHandlers.ofString())
            .thenApply(HttpResponse::body)
            .toCompletableFuture();
    }
    private String generateHash(String apiKey, String ts, String privateKey) {
        try {
            var md = MessageDigest.getInstance("MD5");
            md.update((ts + privateKey + apiKey).getBytes());
            byte[] digested = md.digest();

            BigInteger big = new BigInteger(1, digested);
            String hash = big.toString(16);
            return "0".repeat(32 - hash.length()) + hash;
        } catch (NoSuchAlgorithmException  e) {
            throw new RuntimeException(e.getMessage());
        }
    }
    
}
