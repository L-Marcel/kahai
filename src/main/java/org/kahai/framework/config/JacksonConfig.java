package org.kahai.framework.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import org.kahai.framework.dtos.request.QuestionRequestBody;
import org.reflections.Reflections;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Set;

@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper(List<JacksonSubtypeConfigurer> configurers) {
        ObjectMapper objectMapper = new ObjectMapper();

        configureFrameworkSubtypes(objectMapper);

        for (JacksonSubtypeConfigurer configurer : configurers) {
            configurer.configure(objectMapper);
        }

        return objectMapper;
    }

    private void configureFrameworkSubtypes(ObjectMapper objectMapper) {
        String packageName = "org.kahai.framework.dtos.request";
        Reflections reflections = new Reflections(packageName);
        Set<Class<? extends QuestionRequestBody>> subTypes = reflections.getSubTypesOf(QuestionRequestBody.class);

        for (Class<? extends QuestionRequestBody> subType : subTypes) {
            String typeName = subType.getSimpleName().replace("RequestBody", "");
            objectMapper.registerSubtypes(new NamedType(subType, typeName));
        }
    }
}