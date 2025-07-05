package org.kahai.framework.transients;

import java.time.Duration;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ScheduledFuture;

import org.kahai.framework.errors.RoomScheduleNotFound;
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
    private Duration duration;
    private Optional<ScheduledFuture<?>> scheduled = Optional.empty();

    public Room(String code, Game game, Duration duration) {
        this(code, game);
        this.duration = duration;
    };

    public Room(String code, Game game) {
        this.duration = Duration.ZERO;
        this.code = code;
        this.session = UUID.randomUUID();
        this.game = game;
        this.participants = Collections.synchronizedList(
            new LinkedList<Participant>()
        );
    };

    public void cancelScheduled() throws RoomScheduleNotFound {
        if(this.scheduled.isEmpty())
            throw new RoomScheduleNotFound();
        
        this.scheduled.get().cancel(false);
        this.scheduled = Optional.empty();
    };
};