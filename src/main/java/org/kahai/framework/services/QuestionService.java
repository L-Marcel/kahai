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
import org.kahai.framework.errors.VariantsDistributionStrategyNotDefined;
import org.kahai.framework.events.RoomEventPublisher;
import org.kahai.framework.models.Game;
import org.kahai.framework.questions.ConcreteQuestion;
import org.kahai.framework.questions.Question;
import org.kahai.framework.questions.storage.QuestionStorage;
import org.kahai.framework.questions.variants.QuestionVariant;
import org.kahai.framework.repositories.ConcreteQuestionRepository;
import org.kahai.framework.services.queue.QuestionVariantsRequest;
import org.kahai.framework.services.strategies.VariantsDistributionStrategy;
import org.kahai.framework.transients.Participant;
import org.kahai.framework.transients.Room;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuestionService {
    private static final Logger log = LoggerFactory.getLogger(QuestionService.class);

    @Autowired
    private ConcreteQuestionRepository questionRepository;

    @Autowired
    private RoomEventPublisher roomEventPublisher;

    @Autowired
    private QuestionStorage storage;

    @Autowired
    private AgentGenAI pedagogicalAgent;

    private Set<String> generatingRooms = ConcurrentHashMap.newKeySet();
    
    private VariantsDistributionStrategy distributionStrategy;

    private VariantsDistributionStrategy getDistributionStrategy() {
        if (this.distributionStrategy == null)
            throw new VariantsDistributionStrategyNotDefined();
        return this.distributionStrategy;
    };

    public void setDistributionStrategy(VariantsDistributionStrategy strategy) {
        this.distributionStrategy = strategy;
        log.info("Estratégia de distribuição de questões alterada!");
    };

    public List<Question> findQuestionsByGame(Game game) {
        List<ConcreteQuestion> concretes = this.questionRepository.findAllByGame(game);
        
        List<Question> quetions = concretes.stream()
            .map((concrete) -> storage.load(concrete.getUuid()))
            .collect(Collectors.toList());

        return quetions;
    };

    public Question findQuestionById(UUID uuid) throws QuestionNotFound {
        ConcreteQuestion concrete = this.questionRepository.findByUuid(uuid)
            .orElseThrow(QuestionNotFound::new);
        
        return storage.load(concrete.getUuid());
    };

    public void startVariantsGeneration(Question question, Room room) {
        this.startVariantsGeneration(question, room, (variants) -> {
            log.info("Variantes da pergunta ({}) geradas!", question.getRoot().getUuid());
            this.roomEventPublisher.emitVariantsGenerated(room, variants);
        });
    };

    private void startVariantsGeneration(
        Question question, 
        Room room, 
        AgentGenAICallback callback
    ) {
        log.info("Iniciando geração das variantes da pergunta ({})!", question.getRoot().getUuid());
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
            log.info("Selecionando e enviando variante da pergunta ({})!", original.getRoot().getUuid());
            for (Participant participant : participants) {
                Optional<QuestionVariant> selected = this.getDistributionStrategy()
                    .selectVariant(
                        participant, 
                        variants
                    );
                
                if(selected.isEmpty()) continue;
                
                selected.get().getRoot().setOriginal(original);
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
                variant.getRoot().setOriginal(original);
            };

            variants.addAll(quetionVariants);
        };

        synchronized(participants) {
            log.info("Selecionando e enviando variantes das perguntas da sala ({})!", room.getCode());

            for (Participant participant : participants) {
                List<QuestionVariant> selecteds = this.getDistributionStrategy()
                    .selectVariants(
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
