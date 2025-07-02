package org.kahai.framework.factory;

import java.util.ArrayList;
import java.util.List;
import org.kahai.framework.dtos.request.AnswerRequestBody;
import org.kahai.framework.dtos.request.CreateQuestionRequestBody;
import org.kahai.framework.models.Answer;
import org.kahai.framework.models.questions.ConcreteQuestion;
import org.kahai.framework.models.questions.MultipleChoiceQuestion;
import org.kahai.framework.models.questions.Question;
import org.kahai.framework.models.questions.QuestionOfTrueOrFalse;
import org.kahai.framework.models.questions.QuestionWithFeedback;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
@Component
public class QuestionFactory {

    public Question createDecoratedQuestion(ConcreteQuestion baseQuestion, CreateQuestionRequestBody requestBody) {
        
        Question decoratedQuestion = baseQuestion;
        String type = requestBody.getQuestionType();

        if (type == null || type.isBlank()) {
            return decoratedQuestion; 
        }

        switch (type) {
            case "multipleChoice":
                if (requestBody.getOptions() != null && !requestBody.getOptions().isEmpty()) {
                    decoratedQuestion = new MultipleChoiceQuestion(decoratedQuestion, requestBody.getOptions());
                }
                break;

            case "trueOrFalse":
                decoratedQuestion = new QuestionOfTrueOrFalse(decoratedQuestion);
                break;
            
            case "mcqWithFeedback":
                 if (requestBody.getOptions() != null && !requestBody.getOptions().isEmpty()) {
                    decoratedQuestion = new MultipleChoiceQuestion(decoratedQuestion, requestBody.getOptions());
                }
                if (requestBody.getFeedback() != null && !requestBody.getFeedback().isBlank()) {
                    decoratedQuestion = new QuestionWithFeedback(decoratedQuestion, requestBody.getFeedback());
                }
                break;

            default:
                break;
        }


        return decoratedQuestion;
    }
}