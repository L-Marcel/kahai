package org.kahai.framework.questions.variants;

import java.util.List;
import java.util.UUID;

import org.kahai.framework.models.Difficulty;
import org.kahai.framework.questions.Question;
import org.kahai.framework.questions.variants.response.ConcreteQuestionVariantResponse;
import org.kahai.framework.questions.variants.response.QuestionVariantResponse;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public final class ConcreteQuestionVariant implements QuestionVariant {
    private UUID uuid = UUID.randomUUID();
    
    private String question;
    private Difficulty difficulty = Difficulty.NORMAL;
    private List<String> options;

    @JsonProperty(required = false)
    private Question original;

    @JsonProperty(required = false)
    private UUID session;

    public ConcreteQuestionVariant(
        String question,
        Difficulty difficulty,
        List<String> options
    ) {
        this.question = question;
        this.difficulty = difficulty;
        this.options = options;
    };

    public ConcreteQuestionVariant(
        String question,
        Difficulty difficulty,
        List<String> options,
        UUID session
    ) {
        this.question = question;
        this.difficulty = difficulty;
        this.options = options;
    };

    @Override
    public ConcreteQuestionVariant getRoot() {
        return this;
    };

    @Override
    public QuestionVariantResponse toResponse(Boolean hasAnswer) {
        return new ConcreteQuestionVariantResponse(this, hasAnswer);
    };
};
