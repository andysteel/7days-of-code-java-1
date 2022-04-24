package com.gmail.andersoninfonet.services;

import java.util.concurrent.CompletableFuture;

public interface ApiClient {
    CompletableFuture<String> getBody(String apiKey);
}
