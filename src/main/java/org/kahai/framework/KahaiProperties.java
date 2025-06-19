package org.kahai.framework;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@ConfigurationProperties(prefix = "kahai")
public class KahaiProperties {
    @Getter
    private final Jwt jwt = new Jwt();

    @Getter
    @Setter
    public static class Jwt {
        private String secret = "";
        private long expiration = 24 * 60 * 60 * 1000L;
    };
    
    @Getter
    private final Cors cors = new Cors();
    
    @Setter
    public static class Cors {
        private String allowedOrigins = "http://localhost:5173";

        public String[] getAllowedOrigins() {
            return KahaiUtils.stringToArray(this.allowedOrigins);
        };
    };

    @Getter
    private final WebSocket websocket = new WebSocket();

    @Setter
    public static class WebSocket {
        @Getter
        private String endpoint = "/websocket";

        private String allowedOrigins = "*";

        public String[] getAllowedOrigins() {
            return KahaiUtils.stringToArray(this.allowedOrigins);
        };

        @Getter
        private String simpleBroker = "/channel/events/rooms";

        @Getter
        private String applicationDestinationPrefixes = "/channel/triggers/rooms";
    };

    @Getter
    private final AI ai = new AI();
    
    @Setter
    public static class AI {
        @Getter
        private String model;

        @Getter
        private float temperature = 0;

        private String keys = "";

        public String[] getKeys() {
            return KahaiUtils.stringToArray(this.keys);
        };
    };
};