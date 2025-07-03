package org.kahai.framework.dtos.response;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.kahai.framework.models.Answer;
import org.kahai.framework.models.Context;
import org.kahai.framework.models.questions.Question;
import org.kahai.framework.models.questions.ConcreteQuestion;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionResponse {
    private UUID uuid;
    private String question;
    private List<String> contexts;
    private List<String> answers; 

    public QuestionResponse(Question question) {
        ConcreteQuestion root = question.getRoot();

        this.uuid = root.getUuid();
        this.question = root.getQuestion();
        
        if (root.getContexts() != null) {
            this.contexts = root.getContexts().stream()
                .map(Context::getName) 
                .collect(Collectors.toList()); 
        };
        
        if (root.getAnswers() != null) {
            this.answers = root.getAnswers().stream()
                .map(Answer::getAnswer)
                .collect(Collectors.toList());
        };
    };
};