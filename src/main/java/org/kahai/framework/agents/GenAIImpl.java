package org.kahai.framework.agents;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.kahai.framework.KahaiProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class GenAIImpl implements GenAI {
    private static final Logger log = LoggerFactory.getLogger(GenAI.class);
    private static final String API_URL = "https://openrouter.ai/api/v1/chat/completions";

    private final float temperature;
    private final String model;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    @Autowired
    private KeySource keySource;

    public GenAIImpl(KahaiProperties properties) {
        this.temperature = properties.getAi().getTemperature();
        this.model = properties.getAi().getModel();
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    };

    @Override
    public void request(
        String systemPrompt, 
        String prompt, 
        GenAICallback callback
    ) {
        try {
            Map<String, Object> requestBody = Map.of(
                "model", this.model,
                "temperature", this.temperature,
                "messages", List.of(
                    Map.of(
                        "role", "system", 
                        "content", systemPrompt
                    ),
                    Map.of(
                        "role", "user", 
                        "content", prompt
                    )
                )
            );

            String json = objectMapper.writeValueAsString(requestBody);

            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + this.keySource.getKey())
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

            httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept((responseBody) -> {
                    try {
                        JsonNode root = objectMapper.readTree(responseBody);
                        String responseText = root
                            .path("choices")
                            .get(0)
                            .path("message")
                            .path("content")
                            .asText();
                        
                        callback.accept(Optional.of(responseText));
                    } catch (Exception e) {
                        callback.accept(Optional.empty());
                        log.error("Erro ao processar a resposta do agente!\n\n" + e.getMessage() + "\n");
                    };
                })
                .exceptionally((e) -> {
                    callback.accept(Optional.empty());
                    return null;
                });
        } catch (Exception e) {
            log.error("Erro ao enviar requisição para o agente!\n\n" + e.getMessage() + "\n");
            callback.accept(Optional.empty());
        };
    };
};