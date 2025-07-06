package org.kahai.framework.errors;

public class ValidationStepError extends RuntimeException {
    public ValidationStepError(String message) {
        super(message);
    };
};
