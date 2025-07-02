package org.kahai.framework.dtos.request;

import java.util.List;

import org.kahai.framework.models.questions.Question;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = CreateConcreteQuestionRequestBody.class, name = "CreateConcreteQuestionRequestBody"),
})
public interface CreateQuestionRequestBody {

    Question toQuestion();
}
