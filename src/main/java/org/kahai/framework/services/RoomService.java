package org.kahai.framework.services;

import java.security.SecureRandom;

import org.kahai.framework.errors.GameNotFound;
import org.kahai.framework.errors.ParticipantAlreadyInRoom;
import org.kahai.framework.errors.RoomNotFound;
import org.kahai.framework.errors.UserRoomAlreadyExists;
import org.kahai.framework.events.RoomEventPublisher;
import org.kahai.framework.models.Game;
import org.kahai.framework.models.User;
import org.kahai.framework.repositories.RoomRepository;
import org.kahai.framework.services.strategies.RoomEventStrategy;
import org.kahai.framework.transients.Room;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.Getter;

@Service
public class RoomService {
    private static final Logger log = LoggerFactory.getLogger(RoomService.class);

    public static Logger getLogger() {
        return log;
    };

    @Autowired
    private RoomRepository repository;

    @Autowired
    private RoomEventPublisher roomEventPublisher;

    //private ScheduledThreadPoolExecutor scheduler;

    @Getter
    private RoomEventStrategy roomEventStrategy;

    // public RoomService() {
    //     this.scheduler = new ScheduledThreadPoolExecutor(10);
    // };

    public void setRoomEventStrategy(RoomEventStrategy strategy) {
        this.roomEventStrategy = strategy;
        log.info("Estratégia de gerenciamento de sala foi alterada!");
        this.roomEventStrategy.setRoomEventPublisher(roomEventPublisher);
        this.roomEventStrategy.setRoomRepository(repository);
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
        return repository.findByCode(code)
            .orElseThrow(RoomNotFound::new);
    };

    public Room findRoomByGame(
        Game game
    ) throws RoomNotFound {
        return repository.findByGame(game)
            .orElseThrow(RoomNotFound::new);
    };

    public Room findRoomByUser(
        User user
    ) throws RoomNotFound {
        return repository.findByUser(user)
            .orElseThrow(RoomNotFound::new);
    };

    public Room createRoom(
        Game game
    ) throws GameNotFound, UserRoomAlreadyExists {
        synchronized(repository) {
            if(repository.existsByUser(game.getOwner())) 
            throw new UserRoomAlreadyExists();

            String code = this.generateCode(6);
            while(repository.existsByCode(code)) 
                code = code.concat(this.generateCode(2));
            
            Room room = new Room(
                code, 
                game
            );

            repository.add(room);

            log.info("Nova sala ({}) criada!", room.getCode());
            return room;
        }
    };

    public void closeRoom(
        Room room
    ) throws RoomNotFound, ParticipantAlreadyInRoom {
        if (roomEventStrategy == null) {
            throw new IllegalStateException("RoomEventStrategy não configurada no RoomService.");
        };
        
        roomEventStrategy.onClose(room);
        log.info("Sala ({}) fechada!", room.getCode());
        roomEventPublisher.emitRoomClosed(room);
    };

    public void roomTimeExceeded(
        Room room
    ) {
        roomEventStrategy.onDurationExceeded(room);
        log.info("Tempo da sala ({}) finalizado!", room.getCode());
    };
};
