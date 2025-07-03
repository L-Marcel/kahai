package org.kahai.framework.repositories;

import java.util.UUID;

import org.kahai.framework.models.Game;
import org.kahai.framework.models.ParticipantAnswer;
import org.kahai.framework.models.Question;
import org.kahai.framework.models.User;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@Repository
public interface ParticipantAnswerRepository extends JpaRepository<ParticipantAnswer, UUID> {
    public List<ParticipantAnswer> findByGame(Game game);
    public List<ParticipantAnswer> findByQuestion(Question question);
    public List<ParticipantAnswer> findByOwner(User owner);
};