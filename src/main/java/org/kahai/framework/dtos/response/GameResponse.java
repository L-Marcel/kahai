package org.kahai.framework.dtos.response;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.kahai.framework.models.Game;
import org.kahai.framework.questions.Question;
import org.kahai.framework.questions.response.QuestionResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GameResponse {
    private UUID uuid;
    private UUID owner;
    private String title;
    private List<QuestionResponse> questions;

    public GameResponse(Game game) {
        this.uuid = game.getUuid();
        this.owner = game.getOwner().getUuid();
        this.title = game.getTitle();
        if (game.getQuestions() != null) { 
            this.questions = game.getQuestions()
                .stream()
                .map(Question::toResponse) 
                .collect(Collectors.toList());
        } else {
            this.questions = List.of();
        };
    };

    public static List<GameResponse> mapFromList(List<Game> games) {
        return games.stream()
            .map(GameResponse::new)
            .toList();
    };
};
