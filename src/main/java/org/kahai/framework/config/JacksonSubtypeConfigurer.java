package org.kahai.framework.config;

import com.fasterxml.jackson.databind.ObjectMapper;

public interface JacksonSubtypeConfigurer {
    void configure(ObjectMapper objectMapper);
}