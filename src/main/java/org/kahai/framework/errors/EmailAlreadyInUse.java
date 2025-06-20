package org.kahai.framework.errors;

import org.springframework.http.HttpStatus;

public final class EmailAlreadyInUse extends HttpError {
    public EmailAlreadyInUse() {
        super("Email já está em uso!", HttpStatus.CONFLICT);
    };
};