package com.gmail.andersoninfonet.utils;

import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import com.gmail.andersoninfonet.model.Content;
import com.gmail.andersoninfonet.model.Series;

public class ParserMarvel implements JsonParser {

    private ParserMarvel() {}

    public static ParserMarvel getInstance() {
        return new ParserMarvel();
    }

    @Override
    public List<? extends Content> parse(String json) {

        String jsonExtracted = extractJson(json);
        String[] jsonSeriesExtracted = extractJsonSeries(jsonExtracted);
        String[] extractedPreFormattedJson = preFormatSerieArrayToCreateSerie(jsonSeriesExtracted);
        
        if(extractedPreFormattedJson != null && extractedPreFormattedJson.length > 0) {
            return  Stream.of(extractedPreFormattedJson)
            .map(ParserMarvel::createSeries)
            .toList();
        }
        return Collections.emptyList();
    }

    private static Series createSeries(String preFormattedMovie) {

        String[] seriesAttributes = preFormattedMovie.split("(?:,)(?=\")");

        String[] titleValue = extractedFromPosition(seriesAttributes, 1);
        var title = titleValue[1];

        String[] yearValue = extractedFromPosition(seriesAttributes, 4);
        var year = Integer.valueOf(yearValue[1]);

        String[] imageValue = extractedFromPosition(seriesAttributes, 9);
        var urlImage = "http:".concat(imageValue[2].replace("\"", "") .concat("/clean.jpg"));
 
        String[] ratingValue = extractedFromPosition(seriesAttributes, 6);
        String rating = null;
        if(!ratingValue[1].equals("\"\"")) {
            rating = ratingValue[1].replace("\"", "");
        } else {
            rating = "";
        }


        return new Series(title, urlImage, rating, year, "Series");
    }

    private static String[] extractedFromPosition(String[] seriesAttributes, int position) {
        return seriesAttributes[position].split(":");
    }
    
    private static String extractJson(String json) {
        if(json != null) {
            var sub = json.substring(json.indexOf("results") + 9, json.length() - 1).substring(1);
            return sub.substring(0, sub.length()-1);
        }
        return "";
    }

    private static String[] extractJsonSeries(String json) {
        if(json != null) {
            return json.split(Pattern.compile("(?<=})(?:,)(?=\\{\"id\")").pattern());
        }
        return new String[]{};
    }

    private static String[] preFormatSerieArrayToCreateSerie(String[] series) {

        if(series != null && series.length > 0) {
    
            return Stream.of(series)
                .map(serie -> {
                    String deleteCollection = serie.replaceAll(Pattern.compile("(?:\"urls\"\\:\\[\\{\\S+}],)").pattern(), "");
                    String preformat = deleteCollection.replaceAll(Pattern.compile("(?:\"thumbnail\"\\:\\{)").pattern(), "");
                    return preformat.substring(0, preformat.indexOf("\"jpg\"}") + 6);
                })
                .map(pre -> pre.replace("{\"", "").replace("\"}", ""))
                .toArray(String[]::new);
        }
        return new String[]{};
    }

}
