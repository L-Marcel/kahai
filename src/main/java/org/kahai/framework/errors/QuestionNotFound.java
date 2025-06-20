package org.kahai.framework.errors;

import org.springframework.http.HttpStatus;

public final class QuestionNotFound extends HttpError {
    public QuestionNotFound() {
        super("Questão não encontrada!", HttpStatus.NOT_FOUND);
    };
};
