package org.kahai.framework.errors;

import org.springframework.http.HttpStatus;

public final class Unauthorized extends HttpError {
    public Unauthorized() {
        super("Não autorizado!", HttpStatus.UNAUTHORIZED);
    };
};
