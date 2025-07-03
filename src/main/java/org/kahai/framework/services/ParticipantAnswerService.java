package org.kahai.framework.services;

import org.kahai.framework.errors.AnswerIsNull;
import org.kahai.framework.models.Game;
import org.kahai.framework.models.ParticipantAnswer;
import org.kahai.framework.models.Question;
import org.kahai.framework.repositories.ParticipantAnswerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.Optional;

@Service
public class ParticipantAnswerService {
    private final ParticipantAnswerRepository repository;

    public ParticipantAnswerService(ParticipantAnswerRepository participantAnswerRepository) {
        this.repository = participantAnswerRepository;
    }

    public ParticipantAnswer saveParticipantAnswer(ParticipantAnswer answer) {
        if (answer.getAnswers().isEmpty()) {
            throw new AnswerIsNull();
        }
        return repository.save(answer);
    }

    public Optional<ParticipantAnswer> findParticipantAnswerById(UUID uuid) {
        return repository.findById(uuid);
    }

    public List<ParticipantAnswer> findAllParticipantAnswers() {
        return repository.findAll();
    }

    public List<ParticipantAnswer> findAnswersByGame(Game game) {
        return repository.findByGame(game);
    }

    public List<ParticipantAnswer> findAnswersByQuestion(Question question) {
        return repository.findByQuestion(question);
    }

    public List<ParticipantAnswer> findAnswersByNickname(String nickname) {
        return repository.findByNickname(nickname);
    }

    public void deleteAnswersByGame(Game game) {
        repository.deleteByGame(game);
    }
    
    public void deleteAnswersByQuestion(Question question) {
        repository.deleteByQuestion(question);
    }
    
    public List<ParticipantAnswer> findAnswersByNicknameInGame(String nickname, Game game) {
        return repository.findByNicknameAndGame(nickname, game);
    }
    
    public List<ParticipantAnswer> findAnswersByNicknameInQuestion(String nickname, Question question) {
        return repository.findByNicknameAndQuestion(nickname, question);
    }
}
