package com.gmail.andersoninfonet.utils;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * LoadProperties
 * @since 0.1.0
 */
public class LoadProperties {
    
    private LoadProperties() {}

    public static String getValue(String key) {
        var resourceBundle = ResourceBundle.getBundle("application", new Locale("pt-BR"));

        String value = "";
        if(key != null) {
            value = resourceBundle.getString(key);
        }
        
        return value;            
    }
}
