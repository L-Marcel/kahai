package org.kahai.framework.dtos.request;

import org.kahai.framework.models.questions.Question;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public abstract class CreateBaseQuestionRequestBody implements CreateQuestionRequestBody {

    protected CreateQuestionRequestBody wrappee;

    public CreateBaseQuestionRequestBody(CreateQuestionRequestBody wrappee) {
        this.wrappee = wrappee;
    }

    @Override
    public Question toQuestion() {
        return wrappee.toQuestion();
    }
}
