package org.kahai.framework.services;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.kahai.framework.questions.request.QuestionRequest;
import org.kahai.framework.questions.storage.QuestionStorage;
import org.kahai.framework.dtos.request.GameRequest;
import org.kahai.framework.errors.GameNotFound;
import org.kahai.framework.models.Game;
import org.kahai.framework.models.User;
import org.kahai.framework.questions.ConcreteQuestion;
import org.kahai.framework.questions.Question;
import org.kahai.framework.repositories.GameRepository;
import org.kahai.framework.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
public class GameService {
    private static final Logger log = LoggerFactory.getLogger(GameService.class);

    @Autowired
    private QuestionStorage storage;

    @Autowired
    private GameRepository gameRepository;

    public Game findGameById(UUID uuid) {
        return this.gameRepository.findById(uuid)
            .orElseThrow(() -> new GameNotFound());
    };

    public List<Game> findGamesByUser(User user) {
        return this.gameRepository.findByOwnerUuid(user.getUuid());
    };

    public Game cloneGame(Game game, User newOwner) {
        return this.gameRepository.save(game.clone(newOwner));
    };

    @Transactional
    public Game createGame(GameRequest body, User user) {
        Validator validator = new Validator();

        validator.validate("title", body.getTitle())
            .nonempty("O título é obrigatório!")
            .max(45, "O título não pode ter mais de 45 caracteres!");

        for(int i = 0; i < body.getQuestions().size(); i++) {
            QuestionRequest question = body.getQuestions().get(i);
            question.validate(validator, "questions." + i);
        };

        validator.run();
        
        Game game = new Game();
        game.setTitle(body.getTitle());
        game.setOwner(user);

        List<Question> questions = body.getQuestions().stream()
            .map(QuestionRequest::toQuestion)
            .peek(q -> q.getRoot().setGame(game))
            .collect(Collectors.toList());

        List<ConcreteQuestion> concretes = questions
            .stream()
            .map((question) -> question.getRoot())
            .collect(Collectors.toList());

        game.setQuestions(concretes);
        Game newGame = this.gameRepository.save(game);

        List<Question> savedQuestions = new ArrayList<>();
        
        try {
            questions.forEach((question) -> {
                savedQuestions.add(question);
                storage.save(question);
            });
        } catch (Exception e) {
            savedQuestions.forEach((question) -> {
                storage.delete(question);
            });

            throw e;
        };

        log.info("Novo jogo ({}) criado!", newGame.getUuid());
        
        return newGame;
    };
};
