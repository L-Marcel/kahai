package org.kahai.framework.questions.variants;

import org.kahai.framework.questions.variants.response.QuestionVariantResponse;

public interface QuestionVariant {
    public ConcreteQuestionVariant getRoot();
    public QuestionVariantResponse toResponse(Boolean hasAnswer);
};
