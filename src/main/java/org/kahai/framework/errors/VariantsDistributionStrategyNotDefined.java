package org.kahai.framework.errors;

import org.springframework.http.HttpStatus;

public final class VariantsDistributionStrategyNotDefined extends HttpError {
    public VariantsDistributionStrategyNotDefined() {
        super(
            "Estratégia de distribuição de variante não definida!", 
            HttpStatus.NOT_IMPLEMENTED
        );
    };
};