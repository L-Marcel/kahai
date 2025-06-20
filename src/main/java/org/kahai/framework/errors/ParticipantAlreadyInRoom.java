package org.kahai.framework.errors;

import org.springframework.http.HttpStatus;

public final class ParticipantAlreadyInRoom extends HttpError {
    public ParticipantAlreadyInRoom() {
        super(
            "Conflito de participantes, tente usar outro apelido, " + 
            "se não der certo, verifique se já não está na sala.", 
            HttpStatus.CONFLICT
        );
    };
};
