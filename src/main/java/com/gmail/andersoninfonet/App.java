package com.gmail.andersoninfonet;

import java.io.File;
import java.io.PrintWriter;

import com.gmail.andersoninfonet.services.ImbdGetTopMoviesService;
import com.gmail.andersoninfonet.services.MarvelGetSeriesService;
import com.gmail.andersoninfonet.utils.HTMLImdbGenerator;
import com.gmail.andersoninfonet.utils.HTMLMarvelGenerator;
import com.gmail.andersoninfonet.utils.LoadProperties;
import com.gmail.andersoninfonet.utils.ParserImdb;
import com.gmail.andersoninfonet.utils.ParserMarvel;

public class App {
    
    public static void main(String[] args) {

        var marvelApiKey = LoadProperties.getValue("marvel.api.key");
        var imdbKey = LoadProperties.getValue("imdb.api.key");

        var marvelApiClient = MarvelGetSeriesService.getInstance();
        var imdbApiClient = ImbdGetTopMoviesService.getInstance();

        var imdbFile = new File("imdb.html");
        var marvelFile = new File("marvel.html");

        try (var printWriter = new PrintWriter(imdbFile);
            var printMarvelWriter = new PrintWriter(marvelFile);) {
            
            var htmlGenerator = new HTMLImdbGenerator(printWriter);
            var marvelHtmlGenerator = new HTMLMarvelGenerator(printMarvelWriter);

            imdbApiClient
                .getBody(imdbKey)
                .thenApply(ParserImdb.getInstance()::parse)
                .thenAccept(htmlGenerator::generate)
                .join();

            marvelApiClient
                .getBody(marvelApiKey)
                .thenApply(ParserMarvel.getInstance()::parse)
                .thenAccept(marvelHtmlGenerator::generate)
                .join();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    
}
