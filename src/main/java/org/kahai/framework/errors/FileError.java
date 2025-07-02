package org.kahai.framework.errors;

import org.springframework.http.HttpStatus;

public final class FileError extends HttpError {
    public FileError() {
        super("Erro ao carregar ou escrever arquivo!", HttpStatus.INTERNAL_SERVER_ERROR);
    };
};
