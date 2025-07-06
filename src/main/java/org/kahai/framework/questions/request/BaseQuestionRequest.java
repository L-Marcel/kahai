package org.kahai.framework.questions.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseQuestionRequest implements QuestionRequest {
    protected QuestionRequest wrappee;
};
