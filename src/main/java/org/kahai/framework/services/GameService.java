package org.kahai.framework.services;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.kahai.framework.dtos.request.AnswerRequestBody;
import org.kahai.framework.dtos.request.CreateGameRequestBody;
import org.kahai.framework.dtos.request.CreateQuestionRequestBody;
import org.kahai.framework.errors.GameNotFound;
import org.kahai.framework.files.QuestionStorage;
import org.kahai.framework.models.Answer;
import org.kahai.framework.models.Context;
import org.kahai.framework.models.Game;
import org.kahai.framework.models.User;
import org.kahai.framework.models.questions.ConcreteQuestion;
import org.kahai.framework.models.questions.Question;
import org.kahai.framework.repositories.GameRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public final class GameService {
    private static final Logger log = LoggerFactory.getLogger(GameService.class);

    @Autowired
    private QuestionStorage storage;

    @Autowired
    private GameRepository gameRepository
    ; @Autowired
    private ObjectMapper objectMapper;

    public Game findGameById(UUID uuid) throws GameNotFound {
        return this.gameRepository.findById(uuid)
            .orElseThrow(GameNotFound::new);
    };

    public List<Game> findGamesByUser(User user) {
        return this.gameRepository.findByOwnerUuid(user.getUuid());
    };

    public Game createGame(CreateGameRequestBody gameBody, User user) {
        Game game = new Game();
        game.setTitle(gameBody.getTitle()); 
        game.setOwner(user);

        if (gameBody.getQuestions() != null) {

            List<Question> questions = gameBody.getQuestions().stream()
                    .map(CreateQuestionRequestBody::toQuestion)
                    .peek(q -> q.getRoot().setGame(game))
                    .collect(Collectors.toList());

            List<ConcreteQuestion> concretes = questions
                .stream()
                .map((question) -> question.getRoot())
                .collect(Collectors.toList());
            
            game.setQuestions(
                concretes
            );

            Game newGame = gameRepository.save(game);

            questions.forEach((question) -> {
                storage.save(question);
            });

            return newGame;
        } else {
            return gameRepository.save(game);
        }
    }

};
