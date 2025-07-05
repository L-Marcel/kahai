package org.kahai.framework.questions;

import java.util.List;

import org.kahai.framework.questions.response.QuestionResponse;
import org.kahai.framework.questions.variants.QuestionVariant;
import org.kahai.framework.utils.Examples;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface Question {
    public List<Boolean> validate(List<String> candidates);

    @JsonIgnore
    public ConcreteQuestion getRoot();
    
    public QuestionResponse toResponse();

    @JsonIgnore
    public Examples<? extends QuestionVariant> getPromptExamples();
};
