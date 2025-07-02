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
import org.kahai.framework.factory.QuestionFactory;
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
    private GameRepository gameRepository
    ;
    @Autowired
    private QuestionFactory questionFactory; @Autowired
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
    game.setOwner(user);
    game.setTitle(gameBody.getTitle());

    List<ConcreteQuestion> questionsForGame = new ArrayList<>();

    for (CreateQuestionRequestBody questionBody : gameBody.getQuestions()) {
        ConcreteQuestion entity = new ConcreteQuestion();
        entity.setGame(game);
        entity.setQuestion(questionBody.getQuestion());
        
        List<Answer> answerEntities = new ArrayList<>();
        if (questionBody.getAnswers() != null && !questionBody.getAnswers().isEmpty()) {
            for (AnswerRequestBody ansBody : questionBody.getAnswers()) {
                Answer answerEntity = new Answer();
                answerEntity.setText(ansBody.getText());
                answerEntity.setCorrect(ansBody.isCorrect());
                answerEntity.setQuestion(entity);
                answerEntities.add(answerEntity);
            }
        } else if (questionBody.getAnswer() != null && !questionBody.getAnswer().isBlank()) {
            Answer answerEntity = new Answer();
            answerEntity.setText(questionBody.getAnswer());
            answerEntity.setCorrect(true);
            answerEntity.setQuestion(entity);
            answerEntities.add(answerEntity);
        }
        entity.setAnswers(answerEntities);
        
        if (questionBody.getContext() != null && !questionBody.getContext().isEmpty()) {
            List<String> contextNames = questionBody.getContext();
            List<Context> contextEntities = contextNames.stream()
                .map(name -> {
                    Context context = new Context(name);
                    context.setQuestion(entity);
                    return context;
                })
                .collect(Collectors.toList());
            entity.setContexts(contextEntities);
        }

        Question decoratedPojo = questionFactory.createDecoratedQuestion(entity, questionBody);

        try {
            String json = objectMapper.writeValueAsString(decoratedPojo);
            entity.setDecoratorsJson(json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Erro ao serializar decoradores para JSON", e);
        }
        
        questionsForGame.add(entity);
    }

    game.setQuestions(questionsForGame);

    return gameRepository.saveAndFlush(game);
}
};
