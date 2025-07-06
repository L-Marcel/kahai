package org.kahai.framework.utils;

import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class StringUtils {
    public static String indentText(String text, String indentation) {
        if (text == null) return null;
        if (indentation == null || indentation.isEmpty()) return text;
        
        String indentedText = text.replaceAll("(?m)^", indentation);
        return indentedText.stripLeading();
    };

    public static String prettyFormat(String prettyJson, ObjectMapper objectMapper) throws JsonProcessingException {
        JsonNode jsonNode = objectMapper.readTree(prettyJson);
        return objectMapper.writeValueAsString(jsonNode);
    };
};
