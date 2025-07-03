package org.kahai.framework.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.kahai.framework.models.Game;
import org.kahai.framework.models.questions.ConcreteQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConcreteQuestionRepository extends JpaRepository<ConcreteQuestion, UUID> {
    public List<ConcreteQuestion> findAllByGame(Game game);
    public Optional<ConcreteQuestion> findByUuid(UUID uuid);
};
