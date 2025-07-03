package org.kahai.framework.dtos.response;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.kahai.framework.transients.Room;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoomResponse {
    private String code;
    private UUID game;
    private UUID owner;
    private List<ParticipantResponse> participants;

    public RoomResponse(Room room) {
        this.code = room.getCode();
        this.game = room.getGame().getUuid();
        this.owner = room.getGame().getOwner().getUuid();
        this.participants = room.getParticipants()
            .stream()
            .map(ParticipantResponse::new)
            .collect(Collectors.toList());
    };
};
