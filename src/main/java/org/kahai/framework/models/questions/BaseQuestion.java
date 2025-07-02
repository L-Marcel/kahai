package org.kahai.framework.models.questions;


import java.util.List;

public abstract class BaseQuestion implements Question {
    protected Question wrappee;

    public BaseQuestion(Question wrappee) {
        this.wrappee = wrappee;
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
