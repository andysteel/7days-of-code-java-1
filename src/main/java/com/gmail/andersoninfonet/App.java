package com.gmail.andersoninfonet;

import java.io.File;
import java.io.PrintWriter;

import com.gmail.andersoninfonet.services.ImbdGetTopMoviesService;
import com.gmail.andersoninfonet.utils.HTMLGenerator;
import com.gmail.andersoninfonet.utils.JsonParser;
import com.gmail.andersoninfonet.utils.LoadProperties;

public class App {
    
    public static void main(String[] args) {
        var baseUrl = LoadProperties.getValue("api.base.url");
        var apiKey = LoadProperties.getValue("api.key");

        var file = new File("index.html");
        try (var printWriter = new PrintWriter(file);) {
            var htmlGenerator = new HTMLGenerator(printWriter);

            ImbdGetTopMoviesService
                .getInstance()
                .getTopMovies(baseUrl, apiKey)
                .thenApply(JsonParser::extractJson)
                .thenApply(JsonParser::extractJsonMovies)
                .thenApply(JsonParser::preFormatMovieArrayToCreateMovie)
                .thenApply(JsonParser::createMovies)
                .thenAccept(htmlGenerator::generate)
                .join();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    
}
