package org.kahai.framework.dtos.request;

import java.util.List;

import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.kahai.framework.models.Answer;
import org.kahai.framework.models.questions.ConcreteQuestion;
import org.kahai.framework.models.questions.Question;

import com.fasterxml.jackson.annotation.JsonTypeName;

// TODO - No final, após o outro TODO @QuestionTypeRequestBody, 
// não é para esse JsonTypeName ser necessário

// TODO - Criar o BaseQuestionRequestBody, o frontend envia
// o JSON no formato do BaseQuestionRequestBody com o
// ConcreteQuestionRequestBody dentro

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonTypeName("CreateConcreteQuestionRequestBody")
public class ConcreteQuestionRequestBody implements QuestionRequestBody {
    private String question;
    private String answer;
    private List<String> context;

    private Integer correctValue = 0;
    private Integer wrongValue = 0;

    @Override
    public Question toQuestion() {
        ConcreteQuestion concrete = new ConcreteQuestion();
        concrete.setQuestion(question);
        // TODO - Aqui deveria ser tudo answers, não answer
        // isso se repete em varios outros momentos
        Answer answer = new Answer();
        answer.setQuestion(concrete);
        answer.setText(this.answer);
        concrete.setAnswers(List.of(answer));
        concrete.setContexts(List.of());
        concrete.setCorrectValue(correctValue);
        concrete.setWrongValue(wrongValue);
        return concrete;
    };
};
