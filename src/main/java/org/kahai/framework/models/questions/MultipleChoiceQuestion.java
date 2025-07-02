package org.kahai.framework.models.questions;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonTypeName;import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;


@JsonTypeName("multipleChoice") 
public class MultipleChoiceQuestion extends BaseQuestion {

     private List<String> options;

       @JsonCreator
    public MultipleChoiceQuestion(
        @JsonProperty("wrappee") Question question,
        @JsonProperty("options") List<String> options
    ) {
        super(question);
        this.options = options;
    }

    @Override
    public String getPromptFormat() {
        String basePrompt = super.getPromptFormat();
        
        StringBuilder formattedPrompt = new StringBuilder(basePrompt);
        formattedPrompt.append("\n"); 
        
        char optionLabel = 'A';
        for (String option : this.options) {
            formattedPrompt.append(optionLabel++)
                           .append(") ")
                           .append(option)
                           .append("\n");
        }
        
        return formattedPrompt.toString();
    }

    
    @Override
    public List<Boolean> validate(List<String> answers) {
       
        return super.validate(answers);
    }
    public List<String> getOptions() {
        return options;
    }
}