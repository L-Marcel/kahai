package org.kahai.framework.questions.request;

import org.kahai.framework.questions.Question;
import org.kahai.framework.validation.ValidatorChain;

public interface QuestionRequest {
    public void validate(ValidatorChain validator);
    public Question toQuestion();
};
