package org.kahai.framework.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.kahai.framework.dtos.request.JoinRoomRequestBody;
import org.kahai.framework.errors.ParticipantAlreadyInRoom;
import org.kahai.framework.errors.ParticipantNotFound;
import org.kahai.framework.events.RoomEventPublisher;
import org.kahai.framework.models.Question;
import org.kahai.framework.models.User;
import org.kahai.framework.repositories.ParticipantRepository;
import org.kahai.framework.transients.Participant;
import org.kahai.framework.transients.Room;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ParticipantService {
    private static final Logger log = LoggerFactory.getLogger(ParticipantService.class);

    @Autowired
    private ParticipantRepository repository;

    @Autowired
    private RoomEventPublisher roomEventPublisher;

    private boolean isParticipantAlreadyInRoom(
        Room room, 
        Participant candidate
    ) {
        for(Participant participant : room.getParticipants()) {
            String nickname = participant.getNickname().toUpperCase();
            String candidateNickname = candidate.getNickname().toUpperCase();
            boolean nicknameAlreadyInUse = nickname.equals(candidateNickname);
            boolean idAlreadyInUse = participant.getUuid().equals(candidate.getUuid());
            boolean userAlreadyInUse = false;
            Optional<User> user = participant.getUser();
            Optional<User> candidateUser = candidate.getUser();
            if(user.isPresent() && candidateUser.isPresent()) {
                userAlreadyInUse = user.get().getEmail().equals(
                    candidateUser.get().getEmail()
                );
            };

            if(nicknameAlreadyInUse || idAlreadyInUse || userAlreadyInUse) 
                return true;
        };

        return false;
    };
    
    public Participant createParticipant(
        JoinRoomRequestBody body, 
        Room room, 
        User user
    ) throws ParticipantAlreadyInRoom {
        String nickname = body.getNickname();
        Participant participant;

        if(user != null) participant = new Participant(nickname, room, user);
        else participant = new Participant(nickname, room);

        synchronized(room.getParticipants()) {
            boolean alreadyInRoom = this.isParticipantAlreadyInRoom(room, participant);
            if(alreadyInRoom) throw new ParticipantAlreadyInRoom();

            this.repository.add(participant);
            room.getParticipants().add(participant);
        }

        log.info("Novo participante ({}) adicionado na sala ({})!", 
            participant.getUuid(), 
            room.getCode()
        );
        this.roomEventPublisher.emitRoomUpdated(participant.getRoom());

        return participant;
    };

    public Participant findParticipantByUser(
        User user
    ) throws ParticipantNotFound {
        return this.repository.findByUser(user)
            .orElseThrow(ParticipantNotFound::new);
    };

    public Participant findParticipantByUuid(
        UUID uuid
    ) throws ParticipantNotFound {
        return this.repository.findByUuid(uuid)
            .orElseThrow(ParticipantNotFound::new);
    };

    public void removeAllByRoom(Room room) {
        synchronized(room.getParticipants()) {
            for (Participant participant : room.getParticipants()) {
                participant.getRoom().getParticipants().remove(participant);
                this.repository.remove(participant);
            };
            log.info("Participantes da sala ({}) removidos!", room.getCode());
        };
    };

    public void removeParticipant(Participant participant) {
        synchronized(participant.getRoom().getParticipants()) {
            participant.getRoom().getParticipants().remove(participant);
            this.repository.remove(participant);
        };

        log.info("Participante ({}) removido da sala ({})!");
        this.roomEventPublisher.emitRoomUpdated(participant.getRoom());
    };

    public void answerQuestion(
        Question question,
        Participant participant,
        List<String> answers
    ) {
        boolean isCorrect = question.getAnswers()
            .contains(question.getAnswers().getFirst());
        // TODO - Resolver isso aqui com o validate
        
        synchronized(participant) {
            participant.setNetxDifficulty(isCorrect);
            
            if(isCorrect) {
                participant.incrementScore();
                participant.incrementCorrectAnswers();
            } else {
                participant.incrementWrongAnswers();
            };
        }

        log.info("Participante ({}) respondeu a pergunta ({})!", 
            participant.getUuid(), 
            question.getUuid()
        );
        this.roomEventPublisher.emitRoomUpdated(participant.getRoom());
    };
};
