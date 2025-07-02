package org.kahai.framework.dtos.response;

import org.kahai.framework.models.Answer;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnswerResponse {
    private String text;
    public AnswerResponse(Answer answer) {
        this.text = answer.getText();
    }
}