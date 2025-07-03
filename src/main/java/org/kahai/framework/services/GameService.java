package org.kahai.framework.services;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.kahai.framework.dtos.request.GameRequestBody;
import org.kahai.framework.dtos.request.QuestionRequestBody;
import org.kahai.framework.errors.GameNotFound;
import org.kahai.framework.files.QuestionStorage;
import org.kahai.framework.models.Game;
import org.kahai.framework.models.User;
import org.kahai.framework.models.questions.ConcreteQuestion;
import org.kahai.framework.models.questions.Question;
import org.kahai.framework.repositories.GameRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// TODO - Na verdade, está mais para um aviso, 
// tratamento de exceções e validação está em falta

@Service
public final class GameService {
    private static final Logger log = LoggerFactory.getLogger(GameService.class);

    @Autowired
    private QuestionStorage storage;

    @Autowired
    private GameRepository gameRepository; 

    public Game findGameById(UUID uuid) throws GameNotFound {
        return this.gameRepository.findById(uuid)
            .orElseThrow(GameNotFound::new);
    };

    public List<Game> findGamesByUser(User user) {
        return this.gameRepository.findByOwnerUuid(user.getUuid());
    };

    public Game createGame(GameRequestBody body, User user) {
        Game game = new Game();
        game.setTitle(body.getTitle()); 
        game.setOwner(user);

        Game newGame;
        if (body.getQuestions() != null) {
            List<Question> questions = body.getQuestions().stream()
                .map(QuestionRequestBody::toQuestion)
                .peek(q -> q.getRoot().setGame(game))
                .collect(Collectors.toList());

            List<ConcreteQuestion> concretes = questions
                .stream()
                .map((question) -> question.getRoot())
                .collect(Collectors.toList());
            
            game.setQuestions(
                concretes
            );

            newGame = gameRepository.save(game);

            questions.forEach((question) -> {
                storage.save(question);
            });
        } else {
            newGame = gameRepository.save(game);
        }

        log.info("Novo jogo ({}) criado!", newGame.getUuid());

        return newGame;
    };
};
