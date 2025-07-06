package org.kahai.framework.questions.variants;

import java.util.List;
import java.util.UUID;

import org.kahai.framework.models.Difficulty;
import org.kahai.framework.questions.Question;
import org.kahai.framework.questions.variants.response.ConcreteQuestionVariantResponse;
import org.kahai.framework.questions.variants.response.QuestionVariantResponse;
import org.kahai.framework.questions.view.QuestionVariantView;

import com.fasterxml.jackson.annotation.JsonView;

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
    
    @JsonView(QuestionVariantView.Payload.class)
    private String question;

    @JsonView(QuestionVariantView.Payload.class)
    private Difficulty difficulty = Difficulty.NORMAL;

    @JsonView(QuestionVariantView.Payload.class)
    private List<String> options;

    private Question original;

    public ConcreteQuestionVariant(
        String question,
        Difficulty difficulty,
        List<String> options
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
