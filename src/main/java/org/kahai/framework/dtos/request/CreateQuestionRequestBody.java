package org.kahai.framework.dtos.request;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateQuestionRequestBody {
    private String question;
    private List<AnswerRequestBody> answers;  
    private String answer; 
      private String questionType;
    private List<String> context;
    private String feedback;  private List<String> options;

}