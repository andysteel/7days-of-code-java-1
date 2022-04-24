package com.gmail.andersoninfonet.utils;

import java.io.StringReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import com.gmail.andersoninfonet.model.Content;
import com.gmail.andersoninfonet.model.Movie;
import com.gmail.andersoninfonet.responses.ImdbResponse;

import jakarta.json.Json;
import jakarta.json.JsonReader;
import jakarta.json.JsonString;

public class ParserImdb implements JsonParser {
    
    private ParserImdb() {}

    public static ParserImdb getInstance() {
        return new ParserImdb();
    }

    private static String extractJson(String json) {
        if(json != null) {
            return json.substring(json.indexOf("[") + 1, json.indexOf("]"));
        }
        return "";
    }

    private static String[] extractJsonMovies(String json) {
        if(json != null) {
            return json.split(Pattern.compile("(?<=})(?:,)(?=\\{)").pattern());
        }
        return new String[]{};
    }

    public static String[] extractJsonAttributes(String[] movies) {

        if(movies != null && movies.length > 0) {
    
            return Stream.of(movies)
                .flatMap(movie -> Stream.of(movie.replace("{\"", "").replace("\"}", "").split("\",\"")))
                .toArray(String[]::new);
        }
        return new String[]{};
    }

    private static String[] preFormatMovieArrayToCreateMovie(String[] movies) {

        if(movies != null && movies.length > 0) {
    
            return Stream.of(movies)
                .map(movie -> movie.replace("{\"", "").replace("\"}", ""))
                .toArray(String[]::new);
        }
        return new String[]{};
    }

    public static List<String> extractTiTles(String json) {

        String jsonExtracted = extractJson(json);
        String[] jsonMoviesExtracted = extractJsonMovies(jsonExtracted);
        String[] attributes =  extractJsonAttributes(jsonMoviesExtracted);

        if(attributes != null && attributes.length > 0) {
            return Stream.of(attributes)
            .map(att -> {
                String[] keyValue = att.split("\":\"");
                if(keyValue[0].equals("title")) {
                    return keyValue[1];
                }
                return "";
            })
            .filter(title -> !title.isBlank())
            .toList();
        }

        return Collections.emptyList();
    }

    public static List<String> extractUrlImage(String json) {

        String jsonExtracted = extractJson(json);
        String[] jsonMoviesExtracted = extractJsonMovies(jsonExtracted);
        String[] attributes =  extractJsonAttributes(jsonMoviesExtracted);

        if(attributes != null && attributes.length > 0) {
            return Stream.of(attributes)
            .map(att -> {
                String[] keyValue = att.split("\":\"");
                if(keyValue[0].equals("image")) {
                    return keyValue[1];
                }
                return "";
            })
            .filter(title -> !title.isBlank())
            .toList();
        }

        return Collections.emptyList();
    }

    public static List<String> extractAttributeByName(String json, String name) {

        String jsonExtracted = extractJson(json);
        String[] jsonMoviesExtracted = extractJsonMovies(jsonExtracted);
        String[] attributes =  extractJsonAttributes(jsonMoviesExtracted);

        if(attributes != null && attributes.length > 0) {
            return Stream.of(attributes)
            .map(att -> {
                String[] keyValue = att.split("\":\"");
                if(keyValue[0].equals(name)) {
                    return keyValue[1];
                }
                return "";
            })
            .filter(title -> !title.isBlank())
            .toList();
        }

        return Collections.emptyList();
    }

    public static List<Movie> createMovies(String[] preFormattedMovie) {

        if(preFormattedMovie != null && preFormattedMovie.length > 0) {
            return  Stream.of(preFormattedMovie)
            .map(ParserImdb::createMovie)
            .toList();
        }
        return Collections.emptyList();
    }

    private static Movie createMovie(String preFormattedMovie) {

        String[] movieAttributes = preFormattedMovie.split("\",\"");

        String[] titleValue = extractedFromPosition(movieAttributes, 2);
        var title = titleValue[1];

        String[] yearValue = extractedFromPosition(movieAttributes, 4);
        var year = Integer.valueOf(yearValue[1]);

        String[] imageValue = extractedFromPosition(movieAttributes, 5);
        var urlImage = imageValue[1];
 
        String[] ratingValue = extractedFromPosition(movieAttributes, 7);
        var rating = ratingValue[1];

        return new Movie(title, urlImage, rating, year, "Movie");
    }

    private static String[] extractedFromPosition(String[] movieAttributes, int position) {
        return movieAttributes[position].split("\":\"");
    }

    public static List<ImdbResponse> getImdbListResponse(String json) {
        var listResponse = new ArrayList<ImdbResponse>();

        try (JsonReader jsonReader = Json.createReader(new StringReader(json))) {
            var jsonObject = jsonReader.readObject();

            var items = jsonObject.get("items");
            var itemsArray = items.asJsonArray();

            itemsArray.forEach(item -> {
                var rankString = (JsonString)item.asJsonObject().get("rank");
                var yearString = (JsonString)item.asJsonObject().get("year");
                var imDbRatingString = (JsonString)item.asJsonObject().get("imDbRating");
                var imDbRatingCountString = (JsonString)item.asJsonObject().get("imDbRatingCount");

                var rank = Integer.valueOf(rankString.getString());
                var year = Integer.valueOf(yearString.getString());
                var imDbRating = new BigDecimal(imDbRatingString.getString());
                var imDbRatingWithScale = imDbRating.setScale(1, RoundingMode.HALF_UP);
                var imDbRatingCount = Long.valueOf(imDbRatingCountString.getString());
                var id = (JsonString)item.asJsonObject().get("id");
                var title = (JsonString)item.asJsonObject().get("title");
                var fullTitle = (JsonString)item.asJsonObject().get("fullTitle");
                var image = (JsonString)item.asJsonObject().get("image");
                var crew = (JsonString)item.asJsonObject().get("crew");
                
                listResponse.add(new ImdbResponse(
                    id.getString(),
                    rank,
                    title.getString(),
                    fullTitle.getString(),
                    year,
                    image.getString(),
                    crew.getString(),
                    imDbRatingWithScale,
                    imDbRatingCount)
                );
                
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
        return listResponse;
    }

    @Override
    public List<? extends Content> parse(String json) {

        String jsonExtracted = extractJson(json);
        String[] jsonMoviesExtracted = extractJsonMovies(jsonExtracted);
        String[] extractedPreFormattedJson = preFormatMovieArrayToCreateMovie(jsonMoviesExtracted);

        if(extractedPreFormattedJson != null && extractedPreFormattedJson.length > 0) {
            return  Stream.of(extractedPreFormattedJson)
            .map(ParserImdb::createMovie)
            .toList();
        }
        return Collections.emptyList();
    }
}
