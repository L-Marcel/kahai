package org.kahai.framework.errors;

import org.springframework.http.HttpStatus;

public class AccessDenied extends HttpError {
    public AccessDenied() {
        super("Acesso negado!", HttpStatus.FORBIDDEN);
    };
};
