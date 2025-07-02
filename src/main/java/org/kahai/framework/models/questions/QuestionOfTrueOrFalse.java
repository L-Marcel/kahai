package org.kahai.framework.models.questions;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("trueOrFalse")
public class QuestionOfTrueOrFalse extends BaseQuestion { 

    public QuestionOfTrueOrFalse(Question question) {
        super(question);
    }

    @Override
    public String getPromptFormat() {
        return super.getPromptFormat() + "\n(Responda com 'Verdadeiro' ou 'Falso')";
    }
}