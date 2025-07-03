package org.kahai.framework.dtos.request;

import org.kahai.framework.models.questions.Question;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseQuestionRequestBody implements QuestionRequestBody {
    protected QuestionRequestBody wrappee;

    @Override
    public Question toQuestion() {
        return wrappee.toQuestion();
    };
};
