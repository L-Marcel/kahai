package org.kahai.framework.errors;

import org.springframework.http.HttpStatus;

public class MethodNotAllowed extends HttpError {
    public MethodNotAllowed() {
        super("Método não permitido!", HttpStatus.METHOD_NOT_ALLOWED);
    };
};