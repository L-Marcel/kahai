package org.kahai.framework.services;

import java.security.SecureRandom;
import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.kahai.framework.errors.GameNotFound;
import org.kahai.framework.errors.ParticipantAlreadyInRoom;
import org.kahai.framework.errors.RoomEventStrategyNotDefined;
import org.kahai.framework.errors.RoomNotFound;
import org.kahai.framework.errors.UserRoomAlreadyExists;
import org.kahai.framework.events.RoomEventPublisher;
import org.kahai.framework.models.Game;
import org.kahai.framework.models.User;
import org.kahai.framework.repositories.RoomRepository;
import org.kahai.framework.services.strategies.RoomEventStrategy;
import org.kahai.framework.services.strategies.RoomEventStrategyNone;
import org.kahai.framework.transients.Room;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoomService {
    private static final Logger log = LoggerFactory.getLogger(RoomService.class);
    
    @Autowired
    private RoomRepository repository;

    @Autowired
    private RoomEventPublisher roomEventPublisher;

    private ScheduledThreadPoolExecutor scheduler;
    private RoomEventStrategy roomEventStrategy;

    public RoomService() {
        this.scheduler = new ScheduledThreadPoolExecutor(10);
        this.roomEventStrategy = new RoomEventStrategyNone();
    };

    private RoomEventStrategy getEventStrategy() {
        if (this.roomEventStrategy == null)
            throw new RoomEventStrategyNotDefined();
        return this.roomEventStrategy;
    };

    public void setEventStrategy(RoomEventStrategy strategy) {
        this.roomEventStrategy = strategy;
        log.info("Estratégia de gerenciamento de sala foi alterada!");
    };

    private String generateCode(int size) {
        SecureRandom random = new SecureRandom();
        StringBuilder builder = new StringBuilder();
        
        for(int i = 0; i < size; i++) {
            builder.append(random.nextInt(10));
        };

        return builder.toString();
    };

    public Room findRoomByCode(
        String code
    ) throws RoomNotFound {
        return this.repository.findByCode(code)
            .orElseThrow(RoomNotFound::new);
    };

    public Room findRoomByGame(
        Game game
    ) throws RoomNotFound {
        return this.repository.findByGame(game)
            .orElseThrow(RoomNotFound::new);
    };

    public Room findRoomByUser(
        User user
    ) throws RoomNotFound {
        return this.repository.findByUser(user)
            .orElseThrow(RoomNotFound::new);
    };

    public Room createRoom(
        Game game
    ) throws GameNotFound, UserRoomAlreadyExists {
        return this.createRoom(game, Duration.ZERO);
    };

    public Room createRoom(
        Game game,
        Duration duration
    ) throws GameNotFound, UserRoomAlreadyExists {
        synchronized(this.repository) {
            if(this.repository.existsByUser(game.getOwner())) 
                throw new UserRoomAlreadyExists();

            String code = this.generateCode(6);
            while(this.repository.existsByCode(code)) 
                code = code.concat(this.generateCode(2));
            
            Room room = new Room(
                code, 
                game,
                duration
            );

            this.repository.add(room);

            log.info("Nova sala ({}) criada!", room.getCode());
            return room;
        }
    };

    public void startRoomTimer(Room room) {
        room.setScheduled(
            Optional.of(
                this.scheduler.schedule(
                    () -> {
                        room.setScheduled(Optional.empty());
                        this.roomTimeExceeded(room);
                    }, 
                    room.getDuration().toSeconds(), 
                    TimeUnit.SECONDS
                )
            )
        );

        log.info(
            "Temporizador da sala ({}) iniciado com duração de {} segundos!", 
            room.getCode(),
            room.getDuration().toSeconds()
        );
    };

    public void setRoomTimer(Room room, Duration duration) {
        room.cancelScheduled();

        room.setScheduled(
            Optional.of(
                this.scheduler.schedule(
                    () -> {
                        room.setScheduled(Optional.empty());
                        this.roomTimeExceeded(room);
                    }, 
                    duration.toSeconds(), 
                    TimeUnit.SECONDS
                )
            )
        );

        log.info(
            "Temporizador da sala ({}) alterado para a duração de {} segundos!", 
            room.getCode(),
            duration.toSeconds()
        );
    };

    public void stopRoomTimer(Room room) {
        room.cancelScheduled();

        log.info(
            "Temporizador da sala ({}) cancelado!", 
            room.getCode()
        );
    };

    public void closeRoom(
        Room room
    ) throws RoomNotFound, ParticipantAlreadyInRoom {
        this.repository.remove(room);
        this.getEventStrategy().onClose(room);

        log.info("Sala ({}) fechada!", room.getCode());
        this.roomEventPublisher.emitRoomClosed(room);
    };

    public void roomTimeExceeded(
        Room room
    ) {
        this.getEventStrategy().onClose(room);
        this.getEventStrategy().onDurationExceeded(room);

        log.info("Tempo da sala ({}) finalizado!", room.getCode());
        log.info("Sala ({}) fechada!", room.getCode());
        this.roomEventPublisher.emitRoomClosed(room);
    };
};
