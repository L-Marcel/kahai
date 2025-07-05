package org.kahai.framework.errors;

import org.springframework.http.HttpStatus;

public final class RoomEventStrategyNotDefined extends HttpError {
    public RoomEventStrategyNotDefined() {
        super(
            "Estratégia de evento das salas não definida!", 
            HttpStatus.NOT_IMPLEMENTED
        );
    };
};