package org.kahai.framework.dtos.request;

import java.util.LinkedList;
import java.util.List;

import lombok.Setter;
import lombok.Getter;

import org.kahai.framework.models.Answer;
import org.kahai.framework.models.questions.ConcreteQuestion;
import org.kahai.framework.models.questions.Question;

import com.fasterxml.jackson.annotation.JsonTypeName;

@Getter
@Setter
@JsonTypeName("CreateConcreteQuestionRequestBody")
public class CreateConcreteQuestionRequestBody implements CreateQuestionRequestBody {
    private String question;
    private String answer;
    private List<String> context;
    private int correctValue = 0;
    private int wrongValue = 0;

    @Override
    public Question toQuestion() {
        ConcreteQuestion q = new ConcreteQuestion();
        q.setQuestion(question);
        Answer answer = new Answer();
        answer.setQuestion(q);
        answer.setText(this.answer);
        q.setAnswers(List.of(answer));
        q.setContexts(List.of());
        q.setCorrectValue(correctValue);
        q.setWrongValue(wrongValue);
        return q;
    }
}
