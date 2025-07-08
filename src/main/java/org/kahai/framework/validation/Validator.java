package org.kahai.framework.validation;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.kahai.framework.errors.ValidationFieldError;
import org.kahai.framework.errors.ValidationStepError;
import org.kahai.framework.errors.ValidationsError;

public class Validator implements ValidatorChain {
    private final List<Field<?>> fields;
    
    public Validator() {
        this.fields = new LinkedList<>();
    };

    public <T> Field<T> validate(String field, T value) {
        Field<T> newField = new Field<T>(field, value);
        this.fields.add(newField);
        return newField;
    };

    public void run() throws ValidationsError {
        ValidationsError errors = new ValidationsError();
        
        for(Field<?> field : this.fields) {
            try {
                field.validate();
            } catch (ValidationFieldError e) {
                errors.getErrors().add(e);
            };
        };

        if(errors.getErrors().size() > 0) {
            throw errors;
        };
    };

    public class Field<T> {
        private final String field;
        private final T value;
        private final List<Runnable> steps;
        private boolean nullable = false;

        private Field(String field, T value) {
            this.field = field;
            this.value = value;
            this.steps = new LinkedList<>();
        };

        private void validate() throws ValidationFieldError {
            if(this.nullable && this.value == null) return;

            ValidationFieldError error = new ValidationFieldError(this.field);
            for(Runnable step : this.steps) {
                try {
                    step.run();
                } catch (ValidationStepError e) {
                    error.addErrorMessage(e.getMessage());
                }
            };

            if(error.hasAnyError()) throw error;
        };

        public Field<T> nullable() {
            this.nullable = true;
            return this;
        };
        
        public Field<T> nonempty(String error) {
            return this.min(1, error);
        };

        public Field<T> min(Integer size, String error) {
            this.steps.add(() -> {
                Integer length = -1;
                
                if(this.value == null) {
                    throw new ValidationStepError(error);
                } else if(value instanceof String) {
                    String string = (String) value;
                    if(string.isBlank() && size > 0) 
                        throw new ValidationStepError(error);
                    length = string.length();
                } else if(value instanceof Collection) {
                    length = ((Collection<?>) value).size();
                } else if(value.getClass().isArray()) {
                    length = Array.getLength(value);
                } else if(value instanceof Number) {
                    length = ((Number) value).intValue();
                };

                if(length < size)
                    throw new ValidationStepError(error);
            });

            return this;
        };

        public Field<T> max(Integer size, String error) {
            this.steps.add(() -> {
                Integer length = Integer.MAX_VALUE;

                if(this.value == null) {
                    throw new ValidationStepError(error);
                } else if(value instanceof String) {
                    length = ((String) value).length();
                } else if(value instanceof Collection) {
                    length = ((Collection<?>) value).size();
                } else if(value.getClass().isArray()) {
                    length = Array.getLength(value);
                } else if(value instanceof Number) {
                    length = ((Number) value).intValue();
                };

                if(length > size) 
                    throw new ValidationStepError(error);
            });

            return this;
        };

        public Field<T> email(String error) {
            this.steps.add(() -> {
                if(!(this.value instanceof String))
                    throw new ValidationStepError(error);
                else {
                    String email = (String) this.value;

                    if(!email.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) 
                        throw new ValidationStepError(error);
                };
            });

            return this;
        };

        public Field<T> pattern(String regex, String error) {
            this.steps.add(() -> {
                try {
                    Pattern pattern = Pattern.compile(regex);

                    if(!(this.value instanceof CharSequence)) {
                        throw new ValidationStepError(error);
                    };
                
                    CharSequence str = (CharSequence) value;
                    if(!pattern.matcher(str).matches()) {
                        throw new ValidationStepError(error);
                    };
                } catch (PatternSyntaxException e) {
                    throw new ValidationStepError(error);
                }
            });

            return this;
        };

        public Field<T> verify(Function<T, Boolean> condition, String error) {
            this.steps.add(() -> {
                if(this.value == null || !condition.apply(value))
                    throw new ValidationStepError(error);
            });

            return this;
        };
    };
};
