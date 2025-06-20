package org.kahai.framework.agents;

public interface GenAI {
    void request(
        String systemPrompt, 
        String prompt, 
        GenAICallback callback
    );
};
