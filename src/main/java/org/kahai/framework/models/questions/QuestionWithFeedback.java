package org.kahai.framework.models.questions;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("withFeedback")
public class QuestionWithFeedback extends BaseQuestion { 

    private String feedback; 

    public QuestionWithFeedback(Question question, String feedback) {
        super(question);
        this.feedback = feedback;
    }

    public String getFeedback() {
        return this.feedback;
    }
    @Override
    public String getPromptFormat() {
        return super.getPromptFormat() + "\n[Feedback Adicional]: " + this.feedback;
    }
}