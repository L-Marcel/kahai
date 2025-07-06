package org.kahai.framework.agents;

import java.io.IOException;
import java.util.List;

import org.kahai.framework.events.RoomEventPublisher;
import org.kahai.framework.questions.Question;
import org.kahai.framework.questions.variants.QuestionVariant;
import org.kahai.framework.transients.Room;
import org.kahai.framework.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class AgentGenAI {
    private static final Logger log = LoggerFactory.getLogger(AgentGenAI.class);

    @Autowired
    private GenAI genAI;

    @Autowired
    private RoomEventPublisher publisher;

    @Autowired
    private ObjectMapper objectMapper;

    // language=xml
    private final String SYSTEM_PROMPT_TEMPLATE = """
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
                %s
                </example>
            </output>
            <rules>
                <rule>
                A resposta certa informada deve SEMPRE aparecer EXATAMENTE igual nas opções de resposta de cada uma das variações geradas. Sem nenhuma palavra ou símbolo a mais.
                </rule>
                <rule>
                Mesmo que a resposta certa esteja ERRADA, ela DEVE aparecer.
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

    private String buildExamplesPrompt(Question question) throws JsonProcessingException {
        QuestionVariant first = question.getPromptExamples().getFirst();
        QuestionVariant second = question.getPromptExamples().getSecond();
        QuestionVariant third = question.getPromptExamples().getThird();
        
        JavaType listType = this.objectMapper.getTypeFactory()
            .constructCollectionType(
                List.class, 
                QuestionVariant.class
            );

        String json = this.objectMapper
            .writerFor(listType)
            .writeValueAsString(List.of(
                first,
                second,
                third
            ));
        
        return StringUtils.indentText(json, "            ");
    };

    private String buildPrompt(Question question) throws JsonProcessingException {
        return this.objectMapper.writeValueAsString(question.toResponse());
    };

    public void generateRoomQuestionsVariants(
        Question question,
        Room room,
        AgentGenAICallback callback
    ) {
        try {
            String jsonExample = this.buildExamplesPrompt(question);
            String prompt = this.buildPrompt(question);
            
            String systemPrompt = String.format(SYSTEM_PROMPT_TEMPLATE, jsonExample);
            log.debug("Enviando system prompt para o Agente Gen AI!\n\n{}\n", systemPrompt);
            log.debug("Enviando prompt para o Agente Gen AI!\n\n{}\n", prompt);

            publisher.emitGenerationStatus(
                room,
                "Gerando variações"
            );

            genAI.request(
                systemPrompt,
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
                            json = StringUtils.prettyFormat(json, objectMapper);
                            log.debug("Resposta da IA foi obtida!\n\n{}", json);

                            TypeReference<List<QuestionVariant>> typeRef = new TypeReference<>() {};
                            List<QuestionVariant> variants = objectMapper.readValue(
                                json,
                                typeRef
                            );

                            variants.forEach(variant -> variant.getRoot().setOriginal(question));
                            publisher.emitGenerationStatus(
                                room,
                                "Variações geradas com sucesso"
                            );

                            callback.accept(variants);
                        } catch (IOException e) {
                            log.error(
                                "Formato invalido recebido pela IA!\n\n{}\n", 
                                e.getMessage()
                            );
                            publisher.emitGenerationStatus(
                                room,
                                "Formato invalido recebido pela IA"
                            );
                        }
                    }, () -> {
                        log.error(
                            "Falha ao gerar variações da pergunta ({})!", 
                            question.getRoot().getUuid()
                        );
                        publisher.emitGenerationStatus(
                            room,
                            "Falha ao gerar variações"
                        );
                    });
                }
            );
        } catch (Exception e) {
            log.error(
                "Falha ao gerar variações da pergunta ({})!\n\n{}\n", 
                question.getRoot().getUuid(), 
                e.getMessage()
            );
            publisher.emitGenerationStatus(
                room,
                "Falha ao gerar variações"
            );
        };
    };
};