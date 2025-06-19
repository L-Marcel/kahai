package org.kahai.framework.config;

import org.kahai.framework.converters.StringToUuidConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addFormatters(
        @SuppressWarnings("null") FormatterRegistry registry
    ) {
        registry.addConverter(new StringToUuidConverter());
    };
};