package org.kahai.framework.questions.variants;

import org.kahai.framework.questions.variants.response.QuestionVariantResponse;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface QuestionVariant {
    @JsonIgnore
    public ConcreteQuestionVariant getRoot();
    
    public QuestionVariantResponse toResponse(Boolean hasAnswer);
};
