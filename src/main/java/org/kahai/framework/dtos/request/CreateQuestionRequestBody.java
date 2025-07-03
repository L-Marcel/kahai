package org.kahai.framework.dtos.request;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateQuestionRequestBody {
    private String question;
    private List<String> answers;
    private List<String> context;
};
