package org.kahai.framework.dtos.request;

import org.kahai.framework.models.questions.Question;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

// TODO - Escanear os subtipos com um @QuestionTypeRequestBody

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type"
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = ConcreteQuestionRequestBody.class, name = "CreateConcreteQuestionRequestBody"),
})
public interface QuestionRequestBody {
    Question toQuestion();
};
