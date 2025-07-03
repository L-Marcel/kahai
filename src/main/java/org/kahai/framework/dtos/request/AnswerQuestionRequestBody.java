package org.kahai.framework.dtos.request;

import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AnswerQuestionRequestBody {
    private UUID question;
    private UUID participant;
    private List<String> answers;
};
