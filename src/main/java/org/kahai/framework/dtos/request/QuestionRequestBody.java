package org.kahai.framework.dtos.request;

import org.kahai.framework.models.questions.Question;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
public interface QuestionRequestBody {
    Question toQuestion();
};
