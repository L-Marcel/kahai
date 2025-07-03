package org.kahai.framework.transients;

import java.util.List;
import java.util.UUID;

import org.kahai.framework.models.Difficulty;
import org.kahai.framework.models.questions.Question;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public final class QuestionVariant {
    private UUID uuid = UUID.randomUUID();
    private String question;
    private Difficulty difficulty = Difficulty.NORMAL;
    private List<String> options;
    private Question original;

    public QuestionVariant() {
        this.uuid = UUID.randomUUID();
    };
};
