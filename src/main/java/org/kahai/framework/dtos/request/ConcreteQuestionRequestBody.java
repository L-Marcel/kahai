package org.kahai.framework.dtos.request;

import java.util.List;
import java.util.stream.Collectors;

import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.kahai.framework.models.Answer;
import org.kahai.framework.models.questions.ConcreteQuestion;
import org.kahai.framework.models.questions.Question;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConcreteQuestionRequestBody implements QuestionRequestBody {
    private String question;
    private List<String> answers;
    private List<String> context;

    private Integer correctValue = 0;
    private Integer wrongValue = 0;

    @Override
    public Question toQuestion() {
        ConcreteQuestion concrete = new ConcreteQuestion();
        concrete.setQuestion(question);
        List<Answer> answerObjects = this.answers.stream().map(answerText -> {
            Answer answer = new Answer();
            answer.setQuestion(concrete);
            answer.setText(answerText);
            return answer;
        }).collect(Collectors.toList());
        concrete.setAnswers(answerObjects);
        concrete.setContexts(List.of());
        concrete.setCorrectValue(this.correctValue != null ? this.correctValue : 0);
        concrete.setWrongValue(this.wrongValue != null ? this.wrongValue : 0);
        return concrete;
    };
};
