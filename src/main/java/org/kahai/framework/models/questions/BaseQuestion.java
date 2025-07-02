package org.kahai.framework.models.questions;


import java.util.List;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

public abstract class BaseQuestion implements Question {
    
    @JsonUnwrapped
    protected Question wrappee;

    public BaseQuestion(Question question) {
        this.wrappee = question;
    }

    @Override
    public String getPromptFormat() {
        return wrappee.getPromptFormat();
    }

    @Override
    public List<Boolean> validate(List<String> answers) {
        return wrappee.validate(answers);
    }
    
    @Override
    public ConcreteQuestion getRoot() {
        return wrappee.getRoot();
    }
}