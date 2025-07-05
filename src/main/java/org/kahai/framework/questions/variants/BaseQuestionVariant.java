package org.kahai.framework.questions.variants;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public abstract class BaseQuestionVariant implements QuestionVariant {
    protected QuestionVariant wrappee;
};
