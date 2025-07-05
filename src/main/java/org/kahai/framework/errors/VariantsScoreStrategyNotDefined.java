package org.kahai.framework.errors;

import org.springframework.http.HttpStatus;

public final class VariantsScoreStrategyNotDefined extends HttpError {
    public VariantsScoreStrategyNotDefined() {
        super(
            "Estratégia de pontuação de salas não definida!", 
            HttpStatus.NOT_IMPLEMENTED
        );
    };
};