package org.kahai.framework.errors;

import org.springframework.http.HttpStatus;

public final class ParticipantNotFound extends HttpError {
    public ParticipantNotFound() {
        super("Participante não encontrado!", HttpStatus.NOT_FOUND);
    };
};
