package org.kahai.framework.questions.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public abstract class BaseQuestionResponse implements QuestionResponse {
    protected QuestionResponse wrappee;
};
