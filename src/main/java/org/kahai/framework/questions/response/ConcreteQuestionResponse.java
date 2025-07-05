package org.kahai.framework.questions.response;

import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConcreteQuestionResponse implements QuestionResponse {
    private UUID uuid;
    private String question;
    private List<String> contexts;
    private List<String> answers; 
};