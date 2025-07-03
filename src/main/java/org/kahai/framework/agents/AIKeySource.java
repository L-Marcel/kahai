package org.kahai.framework.agents;

import org.kahai.framework.KahaiProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;

@Component
public class AIKeySource implements KeySource {
    @Getter
    private final String[] keys;
    private Integer currentKey = -1;

    public AIKeySource(KahaiProperties properties) {
        this.keys = properties.getAi().getKeys();
    };

    public synchronized String getKey() {
        this.changeCurrentKey();
        if(currentKey < 0) return null;
        return keys[currentKey];
    };

    private void changeCurrentKey() {
        if(keys.length > 0) 
            currentKey = (currentKey + 1) % keys.length;
    };
};
