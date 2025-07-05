package org.kahai.framework.services;

import org.kahai.framework.models.Answer;
import org.kahai.framework.models.Game;
import org.kahai.framework.models.ParticipantAnswer;
import org.kahai.framework.models.User;
import org.kahai.framework.questions.Question;
import org.kahai.framework.repositories.ParticipantAnswerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.Optional;

@Service
public class ParticipantAnswerService {
    @Autowired
    private ParticipantAnswerRepository repository;

    public List<ParticipantAnswer> createParticipantAnswer(
        UUID session, 
        Game game, 
        User user,
        Question question, 
        String nickname,
        List<Answer> answers
    ) {
        List<ParticipantAnswer> participantsAnswers = answers.stream()
            .map((answer) -> {
                return new ParticipantAnswer(
                    session, 
                    game,
                    user,
                    question, 
                    nickname, 
                    answer
                );
            }).collect(Collectors.toList());

        return repository.saveAll(participantsAnswers);
    };

    public Optional<ParticipantAnswer> findParticipantAnswerById(UUID uuid) {
        return repository.findById(uuid);
    };

    public List<ParticipantAnswer> findAllParticipantAnswers() {
        return repository.findAll();
    };

    public List<ParticipantAnswer> findAnswersByGame(Game game) {
        return repository.findByGame(game);
    };

    public List<ParticipantAnswer> findAnswersByQuestion(Question question) {
        return repository.findByQuestion(question.getRoot());
    };

    public List<ParticipantAnswer> findAnswersByUser(User user) {
        return repository.findByOwner(user);
    };
};
