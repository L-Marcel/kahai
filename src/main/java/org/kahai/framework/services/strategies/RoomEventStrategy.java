package org.kahai.framework.services.strategies;

import org.kahai.framework.transients.Room;

public interface RoomEventStrategy {
    public void onClose(Room room);
    public void onStart(Room room);
    public void onDurationExceeded(Room room);
};