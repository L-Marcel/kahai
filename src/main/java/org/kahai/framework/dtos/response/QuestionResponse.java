package org.kahai.framework.dtos.response;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.kahai.framework.models.Answer;
import org.kahai.framework.models.Context;
import org.kahai.framework.models.Question;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuestionResponse {
    private UUID uuid;
    private String question;
    private List<String> answers;
    private List<String> context;

    public QuestionResponse(Question question) {
        this.uuid = question.getUuid();
        this.question = question.getQuestion();
        this.answers = question.getAnswers()
            .stream()
            .map(Answer::getAnswer)
            .collect(Collectors.toList());
        this.context = question.getContexts()
            .stream()
            .map(Context::getName)
            .collect(Collectors.toList());
    };
};
