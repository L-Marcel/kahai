package org.kahai.framework.questions.variants.response;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.kahai.framework.models.Answer;
import org.kahai.framework.models.Context;
import org.kahai.framework.models.Difficulty;
import org.kahai.framework.questions.variants.ConcreteQuestionVariant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConcreteQuestionVariantResponse implements QuestionVariantResponse {
    private UUID uuid;
    private String question;
    private Difficulty difficulty = Difficulty.NORMAL;
    private List<String> options;
    private List<String> contexts;
    private UUID original;
    private List<String> answers;

    public ConcreteQuestionVariantResponse(ConcreteQuestionVariant question, Boolean hasAnswer) {
        this.uuid = question.getUuid();
        this.question = question.getQuestion();
        this.difficulty = question.getDifficulty();
        this.options = question.getOptions();

        this.original = question.getOriginal()
            .getRoot()
            .getUuid();
        
        this.contexts = question.getOriginal()
            .getRoot()
            .getContexts()
            .stream()
            .map(Context::getName)
            .collect(Collectors.toList());

        if(hasAnswer) {
            this.answers = question.getOriginal().getRoot().getAnswers()
                .stream()
                .map(Answer::getAnswer)
                .collect(Collectors.toList());
        };
    };
};
