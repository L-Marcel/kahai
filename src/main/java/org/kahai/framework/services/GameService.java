package org.kahai.framework.services;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.kahai.framework.dtos.request.CreateGameRequestBody;
import org.kahai.framework.dtos.request.CreateQuestionRequestBody;
import org.kahai.framework.errors.GameNotFound;
import org.kahai.framework.models.Answer;
import org.kahai.framework.models.Context;
import org.kahai.framework.models.Game;
import org.kahai.framework.models.Question;
import org.kahai.framework.models.User;
import org.kahai.framework.repositories.GameRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public final class GameService {
    private static final Logger log = LoggerFactory.getLogger(GameService.class);

    @Autowired
    private GameRepository gameRepository;

    public Game findGameById(UUID uuid) throws GameNotFound {
        return this.gameRepository.findById(uuid)
            .orElseThrow(GameNotFound::new);
    };

    public List<Game> findGamesByUser(User user) {
        return this.gameRepository.findByOwnerUuid(user.getUuid());
    };

    public Game createGame(
        CreateGameRequestBody body,
        User user
    ) {
        Game game = new Game();
        game.setTitle(body.getTitle());
        game.setOwner(user);

        List<Question> questions = new ArrayList<>();
        List<CreateQuestionRequestBody> questionBodies = body.getQuestions();
        for (CreateQuestionRequestBody questionBody : questionBodies) {
            Question question = new Question();
            question.setGame(game);
            question.setQuestion(questionBody.getQuestion());

            List<Answer> answers = Answer.fromList(questionBody.getAnswers());
            question.setAnswers(answers);
            answers.forEach((answer) -> answer.setQuestion(question));

            List<Context> contexts = new LinkedList<>();
            List<String> contextNames = questionBody.getContext();
            if (contextNames != null && !contextNames.isEmpty()) {
                for(String contextName : contextNames) {
                    Context context = new Context();
                    context.setName(contextName);
                    context.setQuestion(question);
                    contexts.add(context);
                };
            };

            question.setContexts(contexts);
            questions.add(question);
        };

        game.setQuestions(questions);
        gameRepository.save(game);
        log.info("Novo jogo ({}) criado!", game.getUuid());

        return game;
    };
};
