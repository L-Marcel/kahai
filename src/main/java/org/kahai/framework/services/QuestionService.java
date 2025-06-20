package org.kahai.framework.services;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.kahai.framework.agents.AgentGenAI;
import org.kahai.framework.agents.AgentGenAICallback;
import org.kahai.framework.errors.QuestionNotFound;
import org.kahai.framework.events.RoomEventPublisher;
import org.kahai.framework.models.Game;
import org.kahai.framework.models.Question;
import org.kahai.framework.repositories.QuestionRepository;
import org.kahai.framework.services.queue.QuestionVariantsRequest;
import org.kahai.framework.services.strategies.VariantsDistributionByDifficulty;
import org.kahai.framework.services.strategies.VariantsDistributionStrategy;
import org.kahai.framework.transients.Participant;
import org.kahai.framework.transients.QuestionVariant;
import org.kahai.framework.transients.Room;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.Getter;

@Service
public final class QuestionService {
    private static final Logger log = LoggerFactory.getLogger(QuestionService.class);

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private RoomEventPublisher roomEventPublisher;

    @Autowired
    private AgentGenAI pedagogicalAgent;
    
    @Getter
    private VariantsDistributionStrategy distributionStrategy;

    public void setDistributionStrategy(VariantsDistributionStrategy strategy) {
        this.distributionStrategy = strategy;
        log.info("Estratégia de distribuição de questões alterada!");
    };

    public QuestionService() {
        this.distributionStrategy = new VariantsDistributionByDifficulty();
    };

    private Set<String> generatingRooms = ConcurrentHashMap.newKeySet();
  
    public List<Question> findQuestionsByGame(Game game) {
        return this.questionRepository.findAllByGame(game);
    };

    public Question findQuestionById(UUID uuid) throws QuestionNotFound {
        return this.questionRepository.findByUuid(uuid)
            .orElseThrow(QuestionNotFound::new);
    };

    public void startVariantsGeneration(Question question, Room room) {
        this.startVariantsGeneration(question, room, (variants) -> {
            log.info("Variantes da pergunta ({}) geradas!", question.getUuid());
            this.roomEventPublisher.emitVariantsGenerated(room, variants);
        });
    };

    private void startVariantsGeneration(
        Question question, 
        Room room, 
        AgentGenAICallback callback
    ) {
        log.info("Iniciando geração das variantes da pergunta ({})!", question.getUuid());
        this.pedagogicalAgent.generateRoomQuestionsVariants(
            question, 
            room, 
            callback
        );
    };

    public void startAllVariantsGeneration(Room room) {
        if(generatingRooms.add(room.getCode())) {
            List<Question> questions = this.findQuestionsByGame(room.getGame());

            QuestionVariantsRequest request = new QuestionVariantsRequest(
                room, 
                questions
            );

            log.info("Iniciando geração das variantes das pergunta da sala ({})!", room.getCode());
            this.startAllVariantsGeneration(request);
        };
    };

    private void startAllVariantsGeneration(QuestionVariantsRequest request) {
        Question question = request.getPeddingQuestions().poll();
        if(question == null) {
            this.roomEventPublisher.emitVariantsGenerated(
                request.getRoom(), 
                request.getGenerateVariants()
                    .stream()
                    .collect(Collectors.toList())
            );
            log.info("Variantes das perguntas da sala ({}) geradas!", request.getRoom().getCode());
            generatingRooms.remove(request.getRoom().getCode());
            return;
        };

        this.startVariantsGeneration(
            question, 
            request.getRoom(),
            (variants) -> {
                request.getGenerateVariants().addAll(variants);
                log.info("Restam %d questões para gerar variantes da sala ({})!", 
                    request.getPeddingQuestions().size(), 
                    request.getRoom().getCode()
                );
                this.startAllVariantsGeneration(request);
            }
        );
    };

    public void sendVariant( 
        List<QuestionVariant> variants,
        UUID originalUuid,
        Room room
    ) {
        List<Participant> participants = room.getParticipants();
        Question original = this.findQuestionById(originalUuid);

        synchronized(participants) {
            log.info("Selecionando e enviando variante da pergunta ({})!", original.getUuid());
            for (Participant participant : participants) {
                Optional<QuestionVariant> selected = this.distributionStrategy. selectVariant(
                    participant, 
                    variants
                );
                
                if(selected.isEmpty()) continue;
                
                selected.get().setOriginal(original);
                roomEventPublisher.emitVariantIntended(
                    room, 
                    participant.getUuid(),
                    selected.get()
                );
            };
        }
    };

    public void sendAllVariant(
        Map<UUID, List<QuestionVariant>> mappedVariants,
        Room room
    ) {
        List<Participant> participants = room.getParticipants();
        List<QuestionVariant> variants = new LinkedList<>();

        for(Entry<UUID, List<QuestionVariant>> entry : mappedVariants.entrySet()) {
            UUID originalUuid = entry.getKey();
            Question original = this.findQuestionById(originalUuid);
            
            List<QuestionVariant> quetionVariants = entry.getValue();

            for(QuestionVariant variant : quetionVariants) {
                variant.setOriginal(original);
            };

            variants.addAll(quetionVariants);
        };

        synchronized(participants) {
            log.info("Selecionando e enviando variantes das perguntas da sala ({})!", room.getCode());

            for (Participant participant : participants) {
                List<QuestionVariant> selecteds = this.distributionStrategy. selectVariants(
                    participant, 
                    variants
                );
                
                if(selecteds.isEmpty()) continue;
                
                roomEventPublisher.emitVariantsIntended(
                    room, 
                    participant.getUuid(),
                    selecteds
                );
            };
        }
    };
};
