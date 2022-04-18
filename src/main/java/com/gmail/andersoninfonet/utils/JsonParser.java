package com.gmail.andersoninfonet.utils;

import java.io.StringReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.gmail.andersoninfonet.responses.ImdbResponse;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.json.JsonString;

public class JsonParser {
    
    private JsonParser() {}

    public static String readingJson(String json) {
        try (JsonReader jsonReader = Json.createReader(new StringReader(json))) {
            JsonObject jsonObject = jsonReader.readObject();

            return jsonObject.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<ImdbResponse> imdbListResponse(String json) {
        try (JsonReader jsonReader = Json.createReader(new StringReader(json))) {
            var jsonObject = jsonReader.readObject();

            var listResponse = new ArrayList<ImdbResponse>();
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

            return listResponse;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }
}
