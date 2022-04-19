package com.gmail.andersoninfonet.model;

import java.math.BigDecimal;

public record Movie(String title, String urlImage, BigDecimal rating, int year) {}
