package com.gmail.andersoninfonet.utils;

import java.util.List;

import com.gmail.andersoninfonet.model.Content;

public interface JsonParser {
    
    List<? extends Content> parse(String json);
}
