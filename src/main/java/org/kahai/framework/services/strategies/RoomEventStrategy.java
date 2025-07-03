package org.kahai.framework.services.strategies;

import org.kahai.framework.events.RoomEventPublisher;
import org.kahai.framework.repositories.RoomRepository;
import org.kahai.framework.transients.Room;

public interface RoomEventStrategy {
    public void onClose(Room room);
    public void onStart(Room room);
    public void onDurationExceeded(Room room);
    public void setRoomEventPublisher(RoomEventPublisher publisher);
    public void setRoomRepository(RoomRepository repository);
};