package org.kahai.framework.errors;

import org.springframework.http.HttpStatus;

public final class InvalidCredentials extends HttpError {
    public InvalidCredentials() {
        super("Email ou senha inválidos.", HttpStatus.UNAUTHORIZED);
    };
};
