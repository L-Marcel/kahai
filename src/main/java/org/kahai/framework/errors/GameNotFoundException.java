package org.kahai.framework.errors;

import org.springframework.http.HttpStatus;

public class GameNotFoundException extends HttpError {
    public GameNotFoundException(String message, HttpStatus error) {
        super(message, error);
    };  public GameNotFoundException() {
        super("Jogo não encontrado", HttpStatus.NOT_FOUND);
    }; public GameNotFoundException(String message) {
        super(message, HttpStatus.INTERNAL_SERVER_ERROR);
    };
};