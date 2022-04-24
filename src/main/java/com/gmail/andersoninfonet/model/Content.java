package com.gmail.andersoninfonet.model;

public interface Content extends Comparable<Content> {
    
    String title();
    String urlImage();
    String rating();
    int year();
    String type();
}
