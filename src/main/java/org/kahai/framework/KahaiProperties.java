package org.kahai.framework;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

@Component
@ConfigurationProperties(prefix = "kahai")
@Validated
public class KahaiProperties {
    @Getter
    private final Jwt jwt = new Jwt();

    @Getter
    @Setter
    public static class Jwt {
        @NotBlank
        private String secret;

        @Positive
        private long expiration = 24 * 60 * 60 * 1000L;
    };
    
    @Getter
    private final Cors cors = new Cors();
    
    @Setter
    public static class Cors {
        @NotBlank
        private String allowedOrigins;

        @Getter
        private Boolean allowCredentials = true;

        public String[] getAllowedOrigins() {
            return KahaiUtils.stringToArray(this.allowedOrigins);
        };
    };

    @Getter
    private final WebSocket websocket = new WebSocket();

    @Setter
    public static class WebSocket {
        @Getter
        @NotBlank
        private String endpoint = "/websocket";

        @NotBlank
        private String allowedOrigins = "*";

        public String[] getAllowedOrigins() {
            return KahaiUtils.stringToArray(this.allowedOrigins);
        };

        @Getter
        @NotBlank
        private String simpleBroker = "/channel/events/rooms";

        @Getter
        @NotBlank
        private String applicationDestinationPrefixes = "/channel/triggers/rooms";
    };

    @Getter
    private final AI ai = new AI();
    
    @Setter
    public static class AI {
        @Getter
        @NotBlank
        private String model;

        @Getter
        @PositiveOrZero
        private Float temperature = 0f;

        @NotBlank
        private String keys;

        public String[] getKeys() {
            return KahaiUtils.stringToArray(this.keys);
        };
    };
};