package org.kahai.framework.models.questions;


import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public abstract class BaseQuestion implements Question {
    protected Question wrappee;

    @Override
    public String getPromptFormat() {
        return wrappee.getPromptFormat();
    };

    @Override
    public List<Boolean> validate(List<String> answers) {
        return wrappee.validate(answers);
    };

    @Override
    public ConcreteQuestion getRoot() {
        return wrappee.getRoot();
    };
};
