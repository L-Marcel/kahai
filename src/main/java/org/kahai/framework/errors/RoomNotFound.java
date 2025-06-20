package org.kahai.framework.errors;

import org.springframework.http.HttpStatus;

public final class RoomNotFound extends HttpError {
    public RoomNotFound() {
        super("Sala não encontrada!", HttpStatus.NOT_FOUND);
    };
};