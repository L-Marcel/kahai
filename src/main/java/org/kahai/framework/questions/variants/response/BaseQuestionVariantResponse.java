package org.kahai.framework.questions.variants.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BaseQuestionVariantResponse implements QuestionVariantResponse {
    protected QuestionVariantResponse wrappee;
};
