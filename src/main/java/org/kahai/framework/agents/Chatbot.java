package org.kahai.framework.agents;

public interface Chatbot {
    void request(
        String systemPrompt, 
        String prompt, 
        ChatbotCallback callback
    );
};
