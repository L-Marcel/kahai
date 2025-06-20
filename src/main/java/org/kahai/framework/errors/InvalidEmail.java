package org.kahai.framework.errors;

import org.springframework.http.HttpStatus;

public final class InvalidEmail extends HttpError {
    public InvalidEmail() {
        super("Formato de email inválido.", HttpStatus.BAD_REQUEST);
    };
};
