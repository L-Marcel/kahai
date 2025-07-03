package org.kahai.framework.dtos.response;

import java.util.UUID;

import org.kahai.framework.models.Difficulty;
import org.kahai.framework.transients.Participant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ParticipantResponse {
    private UUID uuid;
    private UUID user;
    private String room;
    private String nickname;
    private Difficulty currentDifficulty = Difficulty.NORMAL;
    private Integer score = 0;

    public ParticipantResponse(Participant participant) {
        this.uuid = participant.getUuid();
        this.nickname = participant.getNickname();
        this.score = participant.getScore();
        this.room = participant.getRoom().getCode();
        this.currentDifficulty = participant.getCurrentDifficulty();
        if(participant.getUser().isPresent()) {
            this.user = participant.getUser().get().getUuid();
        };
    };
};
