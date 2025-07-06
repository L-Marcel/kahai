package org.kahai.framework.validation;

public interface ValidatorChain {
    public <T> Validator.Field<T> validate(String field, T value);
};
