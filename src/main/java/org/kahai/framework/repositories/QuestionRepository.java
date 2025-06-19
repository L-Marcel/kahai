package org.kahai.framework.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.kahai.framework.models.Game;
import org.kahai.framework.models.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends JpaRepository<Question, UUID> {
    public List<Question> findAllByGame(Game game);
    public Optional<Question> findByUuid(UUID uuid);
};
