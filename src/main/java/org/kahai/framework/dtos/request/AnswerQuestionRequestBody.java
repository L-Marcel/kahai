package org.kahai.framework.dtos.request;

import java.util.List;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnswerQuestionRequestBody {
    private UUID question;
    private UUID participant;
    private List<String> answers;
};
