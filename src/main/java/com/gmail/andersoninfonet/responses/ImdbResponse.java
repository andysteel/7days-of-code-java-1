package com.gmail.andersoninfonet.responses;

import java.math.BigDecimal;

public record ImdbResponse(String id, int rank, String title, String fullTitle, int year, String image, String crew, BigDecimal imDbRating, long imDbRatingCount) {
    
}
