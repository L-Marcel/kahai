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
import org.kahai.framework.transients.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoomService {
    @Autowired
    private RoomRepository repository;

    @Autowired
    private RoomEventPublisher roomEventPublisher;

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
    ) throws GameNotFound {
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
            return room;
        }
    };

    public void closeRoom(
        Room room
    ) throws RoomNotFound, ParticipantAlreadyInRoom {
        repository.remove(room);
        roomEventPublisher.emitRoomClosed(room);
    };
};
