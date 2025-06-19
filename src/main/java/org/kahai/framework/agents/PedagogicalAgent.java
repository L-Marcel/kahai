package org.kahai.framework.agents;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.kahai.framework.events.RoomEventPublisher;
import org.kahai.framework.models.Question;
import org.kahai.framework.transients.QuestionVariant;
import org.kahai.framework.transients.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class PedagogicalAgent {
    @Autowired
    private Chatbot chatbot;

    @Autowired
    private RoomEventPublisher publisher;

    @Autowired
    private ObjectMapper objectMapper;

    // language=xml
    private final String SYSTEM_PROMPT = """
        <system>
            <context>
            Você vai receber uma questão, algumas palavras chaves para dar contexto a ela e a resposta certa para criar variações dela.
            </context>
            <role>
            Você é um agente inteligente especializado.
            </role>
            <action>
            Crie variações da questão nos seguintes níveis de dificuldade:

            • Fácil (EASY): pergunta mais simples com 3 ou 4 opções fáceis (incluindo a certa);
            • Médio (NORMAL): pergunta um pouco mais desafiadora com 4 ou 5 opções medianas para acertar (incluindo a certa);
            • Difícil (HARD): pergunta com formulação mais elaborada ou técnica com 6 opções difíceis de acertar (incluindo a certa).
            </action>
            <output>
                <format>
                Como resposta, gere um json no seguinte formato (e não coloque dentro de bloco de código, retorne apenas com texto).
                </format>
                <example>
                [
                    {
                        "difficulty": "EASY",
                        "question": "...",
                        "options": [
                        "...",
                        "...",
                        "..."
                        ]
                    },
                    {
                        "difficulty": "NORMAL",
                        "question": "...",
                        "options": [
                        "...",
                        "...",
                        "...",
                        "..."
                        ]
                    },
                    {
                        "difficulty": "HARD",
                        "question": "...",
                        "options": [
                        "...",
                        "...",
                        "...",
                        "...",
                        "..."
                        ]
                    }
                ]
                </example>
            </output>
            <rules>
                <rule>
                A resposta certa informada deve SEMPRE aparecer EXATAMENTE igual nas opções de resposta de cada uma das variações geradas. Sem nenhuma palavra ou símbolo a mais.
                </rule>
                <rule>
                Não coloque prefixos de listagem nas respostas, ex: A. B. C.; I. II. III. ou 1. 2. 3.
                </rule>
                <rule>
                Se atenta as palavras chaves passadas pelo professor para dar contexto e procure não fugir deste contexto ao gerar as variações.
                </rule>
                <rule>
                Não coloque perguntas como opções de resposta, exceto que a resposta correta também seja uma pergunta.
                </rule>
            </rules>
            <tone>
            Use palavras difíceis na pergunta das variações difíceis e palavras fáceis na pergunta das variações fáceis.
            </tone>
            <language>
            Responda sempre com o mesmo idioma da pergunta. Na dúvida, responda em portugues brasileiro.
            </language>
        </system>
    """;

    private String buildPrompt(Question question) {
        String contexts = question.getContexts().stream()
            .map((context) -> {
                return String.format(
                    // language=xml
                    """
                    <context>%s</context>
                    """, 
                    context.getName()
                );
            }).collect(Collectors.joining("        ")).trim();
                
        return String.format(
            // language=xml
            """
            <prompt>
                <question>%s</question>
                <answer>%s</answer>
                <contexts>%s</contexts>
            </prompt>
            """, 
            question.getQuestion(),
            question.getAnswer(),
            "        \n        " + contexts + "\n    "
        );
    };

    public void generateRoomQuestionsVariants(
        Question question,   
        Room room,
        PedagogicalAgentCallback callback
    ) {
        String prompt = buildPrompt(question);

        System.out.println(prompt);

        publisher.emitGenerationStatus(
            room, 
            "Gerando variações"
        );

        chatbot.request(
            this.SYSTEM_PROMPT, 
            prompt, 
            (response) -> {
                publisher.emitGenerationStatus(
                    room, 
                    "Recebendo resposta da IA"
                );

                response.ifPresentOrElse((output) -> {
                    publisher.emitGenerationStatus(
                        room, 
                        "Formatando respota da IA"
                    );

                    String json = output
                        .replaceAll("(?s)```(?:json)?\\s*(.*?)\\s*```", "$1")
                        .trim();
                    
                    try {
                        List<QuestionVariant> variants = objectMapper.readValue(
                            json,
                            new TypeReference<List<QuestionVariant>>() {}
                        );

                        variants.forEach(variant -> variant.setOriginal(question));

                        publisher.emitGenerationStatus(
                            room, 
                            "Variações geradas com sucesso"
                        );

                        callback.accept(variants);
                    } catch (IOException e) {
                        publisher.emitGenerationStatus(
                            room, 
                            "Formato invalido recebido pela IA"
                        );

                        e.printStackTrace();
                    }
                }, () -> {
                    publisher.emitGenerationStatus(
                        room, 
                        "Falha ao gerar variações"
                    );
                });
            }
        );
    };
};