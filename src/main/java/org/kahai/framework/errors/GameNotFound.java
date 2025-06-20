package org.kahai.framework.errors;

import org.springframework.http.HttpStatus;

public final class GameNotFound extends HttpError {
    public GameNotFound() {
        super("Jogo não encontrado!", HttpStatus.NOT_FOUND);
    };
};
