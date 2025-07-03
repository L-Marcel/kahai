package org.kahai.framework.dtos.response;

import org.kahai.framework.models.Answer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AnswerResponse {
    private String text;

    public AnswerResponse(Answer answer) {
        this.text = answer.getText();
    };
};