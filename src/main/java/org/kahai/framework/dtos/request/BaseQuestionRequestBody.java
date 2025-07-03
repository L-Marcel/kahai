package org.kahai.framework.dtos.request;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.kahai.framework.models.Answer;
import org.kahai.framework.models.Context;
import org.kahai.framework.models.questions.ConcreteQuestion;
import org.kahai.framework.models.questions.Question;
import org.springframework.util.StringUtils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BaseQuestionRequestBody implements QuestionRequestBody {
    private String question;
    private String answer;
    private List<String> context;

    @Override
    public Question toQuestion() {
        ConcreteQuestion concrete = new ConcreteQuestion();
        concrete.setQuestion(this.question);

        if (StringUtils.hasText(this.answer)) {
            Answer answerEntity = new Answer();
            answerEntity.setQuestion(concrete);
            answerEntity.setAnswer(this.answer);

            concrete.setAnswers(List.of(answerEntity));
        } else {
            concrete.setAnswers(Collections.emptyList());
        };

        if (this.context != null && !this.context.isEmpty()) {
            List<Context> contextEntities = this.context.stream().map(contextText -> {
                Context ctx = new Context();
                ctx.setQuestion(concrete);
                ctx.setName(contextText);
                return ctx;
            }).collect(Collectors.toList());
            concrete.setContexts(contextEntities);
        } else {
            concrete.setContexts(Collections.emptyList());
        };

        concrete.setCorrectValue(0);
        concrete.setWrongValue(0);
        return concrete;
    };
};
