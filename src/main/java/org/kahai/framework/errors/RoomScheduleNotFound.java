package org.kahai.framework.errors;

import org.springframework.http.HttpStatus;

public final class RoomScheduleNotFound extends HttpError {
    public RoomScheduleNotFound() {
        super("Não foi encontrado nenhum temporizador em execução para esta sala!", HttpStatus.NOT_FOUND);
    };
};