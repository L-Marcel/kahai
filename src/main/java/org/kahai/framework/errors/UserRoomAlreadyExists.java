package org.kahai.framework.errors;

import org.springframework.http.HttpStatus;

public final class UserRoomAlreadyExists extends HttpError {
    public UserRoomAlreadyExists() {
        super(
            "Usuário já possui uma sala em execução!", 
            HttpStatus.CONFLICT
        );
    };
};
