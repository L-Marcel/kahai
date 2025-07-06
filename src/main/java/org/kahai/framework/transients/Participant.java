package org.kahai.framework.transients;

import java.util.Optional;
import java.util.UUID;

import org.kahai.framework.models.Difficulty;
import org.kahai.framework.models.User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class Participant {
    private UUID uuid = UUID.randomUUID();
    private Room room;
    private Optional<User> user;
    private String nickname;
    private Difficulty currentDifficulty = Difficulty.NORMAL;

    private Integer correctAnswers = 0;
    private Integer wrongAnswers = 0;
    private Integer score = 0;

    public Participant(String nickname, Room room) {
        this.nickname = nickname;
        this.room = room;
        this.user = Optional.empty();
    };

    public Participant(String nickname, Room room, User user) {
        this.nickname = nickname;
        this.room = room;
        this.user = Optional.of(user);
    };
    
    public void incrementCorrectAnswers() {
        this.correctAnswers += 1;
    };
    
    public void incrementWrongAnswers() {
        this.wrongAnswers += 1;
    };

    public Difficulty setNextDifficulty(
        Boolean isCorrect
    ) {
        Difficulty[] difficulties = Difficulty.values();
        Integer index = currentDifficulty.ordinal();

        if (isCorrect && index < difficulties.length - 1) {
            return currentDifficulty = difficulties[index + 1];
        } else if (!isCorrect && index > 0) {
            return currentDifficulty = difficulties[index - 1];
        };

        return currentDifficulty;
    };
};
