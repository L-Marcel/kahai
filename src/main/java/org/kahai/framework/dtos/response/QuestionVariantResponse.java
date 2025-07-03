package org.kahai.framework.dtos.response;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.kahai.framework.models.Answer;
import org.kahai.framework.models.Context;
import org.kahai.framework.models.Difficulty;
import org.kahai.framework.transients.QuestionVariant;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class QuestionVariantResponse {
    private UUID uuid;
    private String question;
    private Difficulty difficulty = Difficulty.NORMAL;
    private List<String> options;
    private List<String> context;
    private UUID original;
    private List<String> answers;

    public QuestionVariantResponse(QuestionVariant question, boolean hasAnswer) {
        this.uuid = question.getUuid();
        this.question = question.getQuestion();
        this.difficulty = question.getDifficulty();
        this.options = question.getOptions();
        this.original = question.getOriginal().getUuid();
        this.context = question.getOriginal()
            .getContexts()
            .stream()
            .map(Context::getName)
            .collect(Collectors.toList());

        if(hasAnswer) {
            this.answers = question.getOriginal().getAnswers()
                .stream()
                .map(Answer::getAnswer)
                .collect(Collectors.toList());
        };
    };
};
