package org.kahai.framework.repositories;

import java.util.UUID;

import org.kahai.framework.models.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, UUID> {};
