package org.kahai.framework.transients;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.kahai.framework.models.Game;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class Room {
    private String code;
    private UUID session;
    private Game game;
    private List<Participant> participants;

    public Room(String code, Game game) {
        this.code = code;
        this.session = UUID.randomUUID();
        this.game = game;
        this.participants = Collections.synchronizedList(
            new LinkedList<Participant>()
        );
    };
};