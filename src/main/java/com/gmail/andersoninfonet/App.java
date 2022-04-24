package com.gmail.andersoninfonet;

import java.io.File;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Stream;

import com.gmail.andersoninfonet.model.Content;
import com.gmail.andersoninfonet.services.ImbdGetTopMoviesService;
import com.gmail.andersoninfonet.services.MarvelGetSeriesService;
import com.gmail.andersoninfonet.utils.HTMLImdbGenerator;
import com.gmail.andersoninfonet.utils.HTMLMarvelGenerator;
import com.gmail.andersoninfonet.utils.HTMLMixedGenerator;
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
        var mixedFile = new File("index.html");

        try (var printWriter = new PrintWriter(imdbFile);
            var printMarvelWriter = new PrintWriter(marvelFile);
            var printMixedWriter = new PrintWriter(mixedFile);) {
            
            var htmlGenerator = new HTMLImdbGenerator(printWriter);
            var marvelHtmlGenerator = new HTMLMarvelGenerator(printMarvelWriter);
            var mixedHtmlGenerator = new HTMLMixedGenerator(printMixedWriter);

            imdbApiClient
                .getBody(imdbKey)
                .thenApply(ParserImdb.getInstance()::parse)
                .thenApply(contents -> contents.stream().sorted(Content::compareTo).toList())
                .thenAccept(htmlGenerator::generate)
                .join();

            marvelApiClient
                .getBody(marvelApiKey)
                .thenApply(ParserMarvel.getInstance()::parse)
                .thenApply(contents -> contents.stream().sorted(Content::compareTo).toList())
                .thenAccept(marvelHtmlGenerator::generate)
                .join();

            var imdbList = imdbApiClient
                .getBody(imdbKey)
                .thenApply(ParserImdb.getInstance()::parse)
                .join();

            var marvelList =  marvelApiClient
                .getBody(marvelApiKey)
                .thenApply(ParserMarvel.getInstance()::parse)
                .join();

            var mixedList = Stream.of(imdbList, marvelList)
                .flatMap(Collection::stream)
                .sorted(Comparator.comparing(Content::title))
                .toList();

            mixedHtmlGenerator.generate(mixedList);
                
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    
}
