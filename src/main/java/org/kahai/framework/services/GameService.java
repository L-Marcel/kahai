package org.kahai.framework.services;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.kahai.framework.dtos.request.GameRequestBody;
import org.kahai.framework.dtos.request.QuestionRequestBody;
import org.kahai.framework.errors.GameNotFound;
import org.kahai.framework.errors.InvalidGameDataException;
import org.kahai.framework.files.QuestionStorage;
import org.kahai.framework.models.Game;
import org.kahai.framework.models.User;
import org.kahai.framework.models.questions.ConcreteQuestion;
import org.kahai.framework.models.questions.Question;
import org.kahai.framework.repositories.GameRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import org.springframework.util.StringUtils;

@Service
public final class GameService {
    private static final Logger log = LoggerFactory.getLogger(GameService.class);

    @Autowired
    private QuestionStorage storage;

    @Autowired
    private GameRepository gameRepository;

    public Game findGameById(UUID uuid) {
        if (uuid == null) {
            throw new InvalidGameDataException("O UUID do jogo não pode ser nulo.", HttpStatus.BAD_REQUEST);
        }
        return this.gameRepository.findById(uuid)
                .orElseThrow(() -> new InvalidGameDataException("Jogo não encontrado com o UUID fornecido.",
                        HttpStatus.NOT_FOUND));
    }

    public List<Game> findGamesByUser(User user) {
        if (user == null || user.getUuid() == null) {
            throw new InvalidGameDataException("Usuário inválido para a busca de jogos.", HttpStatus.BAD_REQUEST);
        }
        return this.gameRepository.findByOwnerUuid(user.getUuid());
    }

    public Game createGame(GameRequestBody body, User user) {
        if (body == null) {
            throw new InvalidGameDataException("O corpo da requisição não pode ser nulo.", HttpStatus.BAD_REQUEST);
        }
        if (!StringUtils.hasText(body.getTitle())) {
            throw new InvalidGameDataException("O título do jogo é obrigatório.", HttpStatus.BAD_REQUEST);
        }
        if (user == null) {
            throw new InvalidGameDataException("O jogo deve pertencer a um usuário.", HttpStatus.BAD_REQUEST);
        }
        Game game = new Game();
        game.setTitle(body.getTitle());
        game.setOwner(user);

        Game newGame;
        List<Question> questions;
        List<ConcreteQuestion> concretes;
        if (body.getQuestions() != null) {
            try {

                questions = body.getQuestions().stream()
                        .map(QuestionRequestBody::toQuestion)
                        .peek(q -> q.getRoot().setGame(game))
                        .collect(Collectors.toList());

                concretes = questions
                        .stream()
                        .map((question) -> question.getRoot())
                        .collect(Collectors.toList());

                game.setQuestions(
                        concretes);
            } catch (Exception e) {
                throw new InvalidGameDataException("Erro ao processar as questões fornecidas: " + e.getMessage(),
                        HttpStatus.INTERNAL_SERVER_ERROR);
            }
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
