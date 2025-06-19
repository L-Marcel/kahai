package org.kahai.framework.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.kahai.framework.agents.PedagogicalAgent;
import org.kahai.framework.dtos.request.SendQuestionVariantsRequestBody;
import org.kahai.framework.errors.QuestionNotFound;
import org.kahai.framework.events.RoomEventPublisher;
import org.kahai.framework.models.Game;
import org.kahai.framework.models.Question;
import org.kahai.framework.repositories.QuestionRepository;
import org.kahai.framework.transients.Participant;
import org.kahai.framework.transients.QuestionVariant;
import org.kahai.framework.transients.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuestionService {
    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private RoomEventPublisher roomEventPublisher;

    @Autowired
    private PedagogicalAgent pedagogicalAgent;
  
    public List<Question> findQuestionsByGame(Game game) {
        return this.questionRepository.findAllByGame(game);
    };

    public Question findQuestionById(UUID uuid) throws QuestionNotFound {
        return this.questionRepository.findByUuid(uuid)
            .orElseThrow(QuestionNotFound::new);
    };

    public void startVariantsGeneration(Question question, Room room) {
        this.pedagogicalAgent.generateRoomQuestionsVariants(
            question,
            room, 
            (variants) -> {
                this.roomEventPublisher.emitVariantsGenerated(room, variants);
            }
        );
    };

    public void sendVariantByDifficulty( 
        SendQuestionVariantsRequestBody body,
        Question original,
        Room room
    ) {
        List<Participant> participants = room.getParticipants();
        List<QuestionVariant> variants = body.getVariants();

        synchronized(participants) {
            for (Participant participant : participants) {
                Optional<QuestionVariant> selected = variants.stream()
                    .filter(
                        (variant) -> variant.getDifficulty() == participant.getCurrentDifficulty()
                    ).findFirst();
    
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
};
