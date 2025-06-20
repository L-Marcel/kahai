package org.kahai.framework.events;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.kahai.framework.dtos.response.QuestionVariantResponse;
import org.kahai.framework.dtos.response.RoomResponse;
import org.kahai.framework.transients.QuestionVariant;
import org.kahai.framework.transients.Room;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public final class RoomEventPublisher {
    private static final Logger log = LoggerFactory.getLogger(RoomEventPublisher.class);

    @Autowired
    private SimpMessagingTemplate simp;

    public void emitRoomUpdated(Room room) {
        log.info("Evento (room-updated) disparado!");
        simp.convertAndSend(
            "/channel/events/rooms/" + room.getCode() + "/updated", 
            new RoomResponse(room)
        );
    };

    public void emitRoomClosed(Room room) {
        log.info("Evento (room-closed) disparado!");
        simp.convertAndSend(
            "/channel/events/rooms/" + room.getCode() + "/closed",
            "Sala fechada!"
        );
    };

    public void emitVariantsGenerated(
        Room room, 
        List<QuestionVariant> variants
    ) {
        log.info("Evento (variants-generated) disparado!");
        simp.convertAndSend(
            "/channel/events/rooms/" + room.getCode() + 
            "/" + room.getGame().getOwner().getUuid().toString() + 
            "/variants",
            variants.stream().map(
                (variant) -> new QuestionVariantResponse(
                    variant, 
                    true
                )
            )
        );
    };

    public void emitVariantIntended(
        Room room, 
        UUID participant, 
        QuestionVariant variant
    ) {
        log.info("Evento (variant-intended) disparado!");
        simp.convertAndSend(
            "/channel/events/rooms/" + room.getCode() + 
            "/participants/" + participant + "/question",
            new QuestionVariantResponse(
                variant, 
                false
            )
        );
    };

    public void emitVariantsIntended(
        Room room, 
        UUID participant, 
        List<QuestionVariant> variants
    ) {
        log.info("Evento (variants-intended) disparado!");
        simp.convertAndSend(
            "/channel/events/rooms/" + room.getCode() + 
            "/participants/" + participant + "/question",
            variants.stream().map((variant) -> new QuestionVariantResponse(
                variant, 
                false
            )).collect(Collectors.toList())
        );
    };
    
    public void emitGenerationStatus(
        Room room, 
        String status
    ) {
        simp.convertAndSend(
            "/channel/events/rooms/" + room.getCode() + "/status",
            status
        );
    };
};
