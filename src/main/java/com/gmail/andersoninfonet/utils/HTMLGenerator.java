package com.gmail.andersoninfonet.utils;

import java.util.List;

import com.gmail.andersoninfonet.model.Content;

public interface HTMLGenerator {
    
    void generate(List<? extends Content> contents);

}
