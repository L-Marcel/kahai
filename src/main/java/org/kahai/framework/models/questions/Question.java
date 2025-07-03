package org.kahai.framework.models.questions;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type",
    visible = true
)
// @JsonSubTypes({
//     @JsonSubTypes.Type(value = ConcreteQuestion.class, name = "concrete"),

//     @JsonSubTypes.Type(value = MultipleChoiceQuestion.class, name = "multipleChoice"),
//     @JsonSubTypes.Type(value = QuestionOfTrueOrFalse.class, name = "trueOrFalse"),
//     @JsonSubTypes.Type(value = QuestionWithFeedback.class, name = "withFeedback")
// })
public interface Question {
    String getPromptFormat();
    List<Boolean> validate(List<String> answers);
    ConcreteQuestion getRoot();
};
