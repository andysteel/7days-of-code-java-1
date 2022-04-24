package com.gmail.andersoninfonet.model;

public record Series(String title, String urlImage, String rating, int year, String type) implements Content {}
