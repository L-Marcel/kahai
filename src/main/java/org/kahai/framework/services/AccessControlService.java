package org.kahai.framework.services;

import org.kahai.framework.errors.AccessDenied;
import org.kahai.framework.models.Game;
import org.kahai.framework.models.User;
import org.kahai.framework.transients.Room;
import org.springframework.stereotype.Service;

@Service
public final class AccessControlService {
    public void checkGameOwnership(User user, Game game) {
        if(!game.getOwner().getUuid().equals(user.getUuid())) 
            throw new AccessDenied();
    };

    public void checkRoomOwnership(User user, Room room) {
        this.checkGameOwnership(user, room.getGame());
    };
};
