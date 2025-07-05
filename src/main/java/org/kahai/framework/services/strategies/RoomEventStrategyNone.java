package org.kahai.framework.services.strategies;

import org.kahai.framework.transients.Room;
import lombok.Setter;

@Setter
public class RoomEventStrategyNone implements RoomEventStrategy {
    @Override
    public void onClose(Room room) {};

    @Override
    public void onStart(Room room) {};

    @Override
    public void onDurationExceeded(Room room) {};
};
