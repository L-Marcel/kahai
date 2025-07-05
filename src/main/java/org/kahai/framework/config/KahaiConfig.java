package org.kahai.framework.config;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.NamedType;

import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.util.ClassUtils;
import org.kahai.framework.Kahai;
import org.kahai.framework.KahaiProperties;
import org.kahai.framework.questions.Question;
import org.kahai.framework.questions.request.QuestionRequest;
import org.kahai.framework.questions.response.QuestionResponse;
import org.kahai.framework.questions.variants.QuestionVariant;
import org.kahai.framework.questions.variants.response.QuestionVariantResponse;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Configuration
public class KahaiConfig {
    private static final Logger log = LoggerFactory.getLogger(KahaiConfig.class);
    private final ApplicationContext context;
    private final Boolean indentOutput;

    public KahaiConfig(ApplicationContext context, KahaiProperties properties) {
        this.context = context;
        this.indentOutput = properties.getJackson().getIndentOutput();
    };

    @JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type",
        visible = false
    ) public interface TypeJson {};

    

    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        
        objectMapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);

        if(this.indentOutput) {
            objectMapper.enable(
                SerializationFeature.INDENT_OUTPUT
            );
        };
        
        String basePackageName = this.getBasePackageName();

        List<Class<?>> bases = List.of(
            QuestionRequest.class, 
            Question.class, 
            QuestionResponse.class,
            QuestionVariant.class,
            QuestionVariantResponse.class
        );

        bases.forEach((base) -> {
            configure(objectMapper, base);
            scan(objectMapper, "org.kahai.framework", base);
            scan(objectMapper, basePackageName, base);
        });

        return objectMapper;
    };

    public <T> void configure(ObjectMapper objectMapper, Class<T> base) {
        objectMapper.addMixIn(base, TypeJson.class);
        log.info(
            "Configuração de polimorfismo aplicada para a classe ({})!", 
            base.getName()
        );
    };
    
    public <T> void scan(ObjectMapper objectMapper, String packageName, Class<T> base) {
        Reflections reflections = new Reflections(packageName);
        Set<Class<? extends T>> subTypes = reflections.getSubTypesOf(base);

        for (Class<?> subType : subTypes) {
            String typeName = subType.getName();
            String[] nameParts = typeName.split("\\.");
            String className = nameParts[nameParts.length - 1];
            
            NamedType type = new NamedType(subType, className);
            objectMapper.registerSubtypes(type);
            log.info("Tipo ({}) registrado!", className);
        };
    };

    private String getBasePackageName() {
        Map<String, Object> beans = this.context.getBeansWithAnnotation(Kahai.class);
        
        if (beans.isEmpty()) {
            log.error("Nenhuma classe principal com a anotação @Kahai foi encontrada!");
            return null;
        };

        Object app = beans.values().iterator().next();
        return ClassUtils.getUserClass(app).getPackageName();
    };
};