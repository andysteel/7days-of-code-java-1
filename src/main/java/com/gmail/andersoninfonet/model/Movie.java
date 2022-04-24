package com.gmail.andersoninfonet.model;

public record Movie(String title, String urlImage, String rating, int year, String type) implements Content {}
