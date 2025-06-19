package org.kahai.framework.config;

import org.kahai.framework.KahaiProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocket
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    private final String endpoint;
    private final String[] allowedOrigins;
    private final String simpleBroker;
    private final String applicationDestinationPrefixes;

    public WebSocketConfig(KahaiProperties properties) {
        this.endpoint = properties.getWebsocket().getEndpoint();
        this.allowedOrigins = properties.getWebsocket().getAllowedOrigins();
        this.simpleBroker = properties.getWebsocket().getSimpleBroker();
        this.applicationDestinationPrefixes = properties.getWebsocket().getApplicationDestinationPrefixes();
    };

    @Override
    public void registerStompEndpoints(
        @SuppressWarnings("null") StompEndpointRegistry registry
    ) {
        registry
            .addEndpoint(this.endpoint)  
            .setAllowedOrigins(this.allowedOrigins);
    };

    @Override
    public void configureMessageBroker(
        @SuppressWarnings("null") MessageBrokerRegistry registry
    ) {
        registry.enableSimpleBroker(this.simpleBroker);
        registry.setApplicationDestinationPrefixes(this.applicationDestinationPrefixes);
    };
};