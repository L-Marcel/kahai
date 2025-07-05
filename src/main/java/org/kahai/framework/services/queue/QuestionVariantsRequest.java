package org.kahai.framework.services.queue;

import java.util.Deque;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;

import org.kahai.framework.questions.Question;
import org.kahai.framework.questions.variants.QuestionVariant;
import org.kahai.framework.transients.Room;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public final class QuestionVariantsRequest {
    private Room room;
    private Deque<Question> peddingQuestions;
    private Deque<QuestionVariant> generateVariants;

    public QuestionVariantsRequest(
        Room room,
        List<Question> questions
    ) {
        this.room = room;
        this.generateVariants = new LinkedBlockingDeque<>();
        this.peddingQuestions = new LinkedBlockingDeque<>(questions);
    };
};