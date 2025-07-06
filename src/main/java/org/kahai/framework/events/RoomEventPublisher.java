package org.kahai.framework.events;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.kahai.framework.dtos.response.RoomResponse;
import org.kahai.framework.questions.variants.QuestionVariant;
import org.kahai.framework.questions.variants.response.QuestionVariantResponse;
import org.kahai.framework.transients.Room;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public final class RoomEventPublisher {
    private static final Logger log = LoggerFactory.getLogger(RoomEventPublisher.class);

    @Autowired
    private SimpMessagingTemplate simp;

    @Autowired
    private ObjectMapper objectMapper;

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

        try {
            List<QuestionVariantResponse> responses = variants.stream().map(
                (variant) -> variant.toResponse(true)
            ).collect(Collectors.toList());

            JavaType listType = this.objectMapper.getTypeFactory()
                .constructCollectionType(
                    List.class, 
                    QuestionVariantResponse.class
                );

            String json = this.objectMapper
                .writerFor(listType)
                .writeValueAsString(responses);

            simp.convertAndSend(
                "/channel/events/rooms/" + room.getCode() +
                "/" + room.getGame().getOwner().getUuid().toString() +
                "/variants",
                json
            );
        } catch (Exception e) {
            log.error(
                "Erro ao enviar dados do evento (variants-generated)!\n\n{}\n", 
                e.getMessage()
            );
        }
    };

    public void emitVariantIntended(
        Room room,
        UUID participant,
        QuestionVariant variant
    ) {
        log.info("Evento (variant-intended) disparado!");
        
        try {
            String json = this.objectMapper
                .writeValueAsString(
                    variant.toResponse(false)
                );

            simp.convertAndSend(
                "/channel/events/rooms/" + room.getCode() +
                "/participants/" + participant + "/question",
                json
            );
        } catch (Exception e) {
            log.error(
                "Erro ao enviar dados do evento (variant-intended)!\n\n{}\n", 
                e.getMessage()
            );
        }
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
            variants.stream().map(
                (variant) -> variant.toResponse(false)
            ).collect(Collectors.toList()));
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
