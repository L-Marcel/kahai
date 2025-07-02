package org.kahai.framework.dtos.response;

import java.util.List;
import java.util.UUID;

import org.kahai.framework.models.Game;

public class GameResponse {

    public UUID getUuid() {
        return this.uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getOwner() {
        return this.owner;
    }

    public void setOwner(UUID owner) {
        this.owner = owner;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<QuestionResponse> getQuestions() {
        return this.questions;
    }

    public void setQuestions(List<QuestionResponse> questions) {
        this.questions = questions;
    }

    private UUID uuid;
    private UUID owner;
    private String title;
    private List<QuestionResponse> questions;

    public GameResponse(Game game) {
        // this.uuid = game.getUuid();
        // this.owner = game.getOwner().getUuid();
        // this.title = game.getTitle();
        // // this.description = game.getDescription();
        // if (game.getQuestions() != null) { 
        //     this.questions = game.getQuestions()
        //             .stream()
        //             .map(QuestionResponse::new) 
        //             .collect(Collectors.toList());
        // } else {
        //     this.questions = List.of();
        // }
    }

    public static List<GameResponse> mapFromList(List<Game> games) {
        return games.stream()
                .map(GameResponse::new)
                .toList();
    }
}
