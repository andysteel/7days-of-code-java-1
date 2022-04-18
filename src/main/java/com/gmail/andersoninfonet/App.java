package com.gmail.andersoninfonet;

import com.gmail.andersoninfonet.services.ImbdGetTopMoviesService;
import com.gmail.andersoninfonet.utils.LoadProperties;

public class App {
    
    public static void main(String[] args) {
        var baseUrl = LoadProperties.getValue("api.base.url");
        var apiKey = LoadProperties.getValue("api.key");

        ImbdGetTopMoviesService.getInstance().getTopMovies(baseUrl, apiKey);
    }
    
}
