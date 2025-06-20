package org.kahai.framework.transients;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.kahai.framework.models.Game;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class Room {
    private String code;
    private Game game;
    private List<Participant> participants;

    public Room(String code, Game game) {
        this.code = code;
        this.game = game;
        this.participants = Collections.synchronizedList(
            new LinkedList<Participant>()
        );
    };
};