package org.kahai.framework.errors;

import org.springframework.http.HttpStatus;

public final class RouteNotFound extends HttpError {
    public RouteNotFound() {
        super("Rota não encontrada!", HttpStatus.NOT_FOUND);
    };
};