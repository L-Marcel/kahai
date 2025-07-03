package org.kahai.framework.errors;

import org.springframework.http.HttpStatus;

public final class AnswerIsNull extends HttpError {
    public AnswerIsNull() {
        super("A resposta não pode ser vazia!", HttpStatus.BAD_REQUEST);
    };
};
