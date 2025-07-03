package org.kahai.framework.errors;

import org.springframework.http.HttpStatus;

public class InvalidGameDataException extends HttpError {
    public InvalidGameDataException(String message, HttpStatus error) {
        super(message, error);
    };  public InvalidGameDataException() {
        super("Erro no jogo", HttpStatus.INTERNAL_SERVER_ERROR);
    }; public InvalidGameDataException(String message) {
        super(message, HttpStatus.INTERNAL_SERVER_ERROR);
    };
};