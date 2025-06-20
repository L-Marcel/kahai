package org.kahai.framework.errors;

import org.springframework.http.HttpStatus;

public final class WeakPassword extends HttpError {
    public WeakPassword() {
        super("Senha deve ter no mínimo 6 caracteres.", HttpStatus.BAD_REQUEST);
    };
};
