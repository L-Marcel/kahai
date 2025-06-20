package org.kahai.framework.errors;

import org.springframework.http.HttpStatus;

public final class MissingFields extends HttpError {
    public MissingFields() {
        super("Todos os campos devem ser preenchidos.", HttpStatus.BAD_REQUEST);
    };
};
