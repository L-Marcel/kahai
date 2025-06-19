package org.kahai.framework.repositories;

import java.util.List;
import java.util.UUID;

import org.kahai.framework.models.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRepository extends JpaRepository<Game, UUID> {
    List<Game> findByOwnerUuid(UUID owner);
};
