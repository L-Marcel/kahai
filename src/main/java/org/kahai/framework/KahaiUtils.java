package org.kahai.framework;

public class KahaiUtils {
    public static String[] stringToArray(String value) {
        String[] result = value.split(",");
        
        for(int i = 0; i < result.length; i++) {
            result[i] = result[i].trim();
        };

        return result;
    };  
};
